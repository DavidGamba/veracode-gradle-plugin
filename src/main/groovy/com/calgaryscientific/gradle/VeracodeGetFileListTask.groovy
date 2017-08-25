/*******************************************************************************
 * MIT License
 *
 * Copyright (c) 2017 Calgary Scientific Incorporated
 *
 * Copyright (c) 2013-2014 kctang
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/

package com.calgaryscientific.gradle

class VeracodeGetFileListTask extends VeracodeTask {
    static final String NAME = 'veracodeGetFileList'

    VeracodeGetFileListTask() {
        description = "Lists all files under the latest build for the application id passed in. If a build id is provided, the files under that build will be listed instead"
        requiredArguments << 'appId' << "buildId${VeracodeTask.OPTIONAL}"
    }

    void run() {
        String xmlResponse
        if (project.hasProperty('buildId')) {
            xmlResponse = uploadAPI().getFileList(project.appId, project.buildId)
        } else {
            xmlResponse = uploadAPI().getFileList(project.appId)
        }
        Node filelist = writeXml('build/file-list.xml', xmlResponse)
        filelist.each() { file ->
            println "${file.@file_name}=${file.@file_status}"
        }
        println ''
        println 'Total files = ' + filelist.children().size()
    }
}