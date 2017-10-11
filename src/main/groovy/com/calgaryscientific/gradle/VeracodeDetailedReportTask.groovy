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

import org.gradle.api.tasks.OutputFile
import groovy.transform.CompileStatic

@CompileStatic
class VeracodeDetailedReportTask extends VeracodeTask {
    static final String NAME = 'veracodeDetailedReport'
    private String build_id

    VeracodeDetailedReportTask() {
        description = "Get the Veracode Scan Report in XML format based on the given 'build_id'"
        requiredArguments << 'build_id'
        build_id = project.findProperty("build_id")
        defaultOutputFile = new File("${project.buildDir}/veracode", "detailed-report-${build_id}.xml")
    }

    // Scan results are not available until the full scan is complete so there is no risk in caching the report.
    @OutputFile
    File getOutputFile() {
        return defaultOutputFile
    }

    void run() {
        XMLIO.writeXml(getOutputFile(), veracodeAPI.detailedReport(build_id))
        printf "report file: %s\n", getOutputFile()
    }
}
