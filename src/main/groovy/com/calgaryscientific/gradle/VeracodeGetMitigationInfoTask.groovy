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

import groovy.transform.CompileStatic

@CompileStatic
class VeracodeGetMitigationInfoTask extends VeracodeTask {
    static final String NAME = 'veracodeGetMitigationInfo'
    String build_id
    String flaw_id_list

    VeracodeGetMitigationInfoTask() {
        description = "Lists flaw information for the given 'build_id' and 'flaw_id_list'"
        requiredArguments << 'build_id' << 'flaw_id_list'
        build_id = project.findProperty("build_id")
        flaw_id_list = project.findProperty("flaw_id_list")
    }

    File getOutputFile() {
        VeracodeMitigationInfo.getFile("${project.buildDir}/veracode", build_id, flaw_id_list)
    }

    void run() {
        Node mitigationInfo = XMLIO.writeXml(getOutputFile(), veracodeAPI.getMitigationInfo(build_id, flaw_id_list))
        VeracodeMitigationInfo.printMitigationInfo(mitigationInfo)
        printf "report file: %s\n", getOutputFile()
    }
}
