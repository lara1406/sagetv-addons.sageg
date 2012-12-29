/*
 *      Copyright 2012 Battams, Derek
 *       
 *       Licensed under the Apache License, Version 2.0 (the "License");
 *       you may not use this file except in compliance with the License.
 *       You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *       Unless required by applicable law or agreed to in writing, software
 *       distributed under the License is distributed on an "AS IS" BASIS,
 *       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *       See the License for the specific language governing permissions and
 *       limitations under the License.
 */
import sagex.SageAPI
import sagex.remote.rmi.RMISageAPI
import groovy.transform.Field

@Field SCRIPT_HOME = new File(System.getProperty('user.home') + '/.groovy/sagetv')

def cache = new File(SCRIPT_HOME, '.cache')
def host = args.find { it ==~ /-h:(?:\d{1,3}\.){3}\d{1,3}(?::\d+)?/ }
if(host) {
    args = args.toList()
    args.remove(host)
    cache.delete()
    cache << host
    host = host.split(':', 3)    
} else if(cache.canRead())
    host = cache.getText().split(':', 3)

if(host && host.size() > 1 && args.size()) {
    def port = host.size() < 3 ? 1098 : host[2].toInteger()
    SageAPI.setProvider(new RMISageAPI(host[1], port))
    println "Connecting to SageTV server at ${host[1..host.size() - 1]}"
}

if(args.size()) {
    def script = args[0]
    args = args.size() > 1 ? args[1..args.size() - 1] : new String[0]
    Binding binding = new Binding()
    binding.setVariable('args', args as String[])
    GroovyShell shell = new GroovyShell(binding)
    def target = new File(SCRIPT_HOME, "${script}.groovy")
    if(target.canRead())
        shell.evaluate(target)
    else
        println "Invalid script!  Does not exist or can't be read! [$target]"
} else {
    println 'sageg: Required command missing!  Valid commands are:\r\n'
    SCRIPT_HOME.eachFileMatch(~/.+\.groovy$/) {
        print it.name.substring(0, it.name.lastIndexOf('.')).padLeft(8)
        def data = it.readLines().find { it ==~ /\/\*\*\* .+ \*\*\*\// }
        if(data)
            print ": ${data.substring(5, data.length() - 5)}"
        println ''
    }
}