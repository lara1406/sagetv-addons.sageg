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
/*** Import a video & link it to a Show: <file> <show_eid> ***/
/*
   Link an imported video to a TV show (for metadata)
   
   Last Updated: 06 Feb 2011
         Author: Derek Battams <derek AT battams DOT ca>
   
   Use this script to link an imported video to a TV show's
   metadata.  I use this script when I miss a recording or
   a recording was bad/incorrect/preempted/etc. and I
   download the recording from the Internet.  I then use
   this script to link the download to its Show details
   such that SageTV will be able to properly mark that
   I've watched the Show, etc.
   
   This script takes two command line arguments in the following
   order:
   
   + The full path of the imported video being linked; this file
     must NOT be registered as a SageTV media object
     
   + The unique SageTV Show external ID the imported video should
     be linked to
*/

import sagex.api.*

if(args.size() < 2) {
   println "You must include two command line arguments: <import_video_file> <ShowEID>"
   return 1
}

def src = args[0]
def sid = args[1]

if(!Utility.IsFilePath(src)) {
   println "${src} does not exist!"
   return 1
}

if(MediaFileAPI.GetMediaFileForFilePath(new File(src)) != null) {
   println "${src} is already a registered SageTV media file!"
   return 1
}

def show = ShowAPI.GetShowForExternalID(sid)
if(show == null) {
   println "ShowEID ${sid} is invalid!"
   return 1
}

def mf = MediaFileAPI.AddMediaFile(new File(src), null)
if(mf == null) {
   println "Failed to add media file!"
   return 1
}

if(!MediaFileAPI.SetMediaFileShow(mf, show)) {
   println "Failed to link show metadata to media file!"
   return 1
}

MediaFileAPI.MoveTVFileOutOfLibrary(mf)

println "Imported '${src}' and linked it to ShowEID '${sid}'!"
return 0