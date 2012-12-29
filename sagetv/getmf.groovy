#!/usr/bin/groovy
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
/*** Get media details for a file path: <file> ***/
import sagex.api.*

if(args[0]) {
	def mf = MediaFileAPI.GetMediaFileForFilePath(new File(args[0]))
	if(mf) {
		println "ID:  ${MediaFileAPI.GetMediaFileID(mf)}"
		println "EID: ${ShowAPI.GetShowExternalID(mf)}"
	} else
		println "File is not a registered media object!"
} else
	println 'Required file path argument missing!'