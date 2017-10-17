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
class VeracodeGetPreScanResultsSandboxTask extends VeracodeTask {
    static final String NAME = 'veracodeSandboxGetPreScanResults'
    String build_id

    VeracodeGetPreScanResultsSandboxTask() {
        group = 'Veracode Sandbox'
        description = "Get the Veracode Pre-Scan Results based on the given 'app_id', 'sandbox_id' and 'build_id'. If no 'build_id' is provided, the latest will be used"
        requiredArguments << 'app_id' << 'sandbox_id'
        optionalArguments << 'build_id'
        app_id = project.findProperty('app_id')
        sandbox_id = project.findProperty('sandbox_id')
        if (project.hasProperty('build_id')) {
            build_id = project.findProperty('build_id')
            defaultOutputFile = new File("${project.buildDir}/veracode", "prescanresults-${app_id}-${sandbox_id}-${build_id}.xml")
        } else {
            defaultOutputFile = new File("${project.buildDir}/veracode", "prescanresults-${app_id}-${sandbox_id}-latest.xml")
        }
    }

    void run() {
        Node xml = XMLIO.writeXml(getOutputFile(), veracodeAPI.getPreScanResults(build_id))
        VeracodePreScanResults.printModuleStatus(xml)
        printf "report file: %s\n", getOutputFile()
    }
}