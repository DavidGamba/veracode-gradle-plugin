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
class VeracodeCreateSandboxTask extends VeracodeTask {
    static final String NAME = 'veracodeCreateSandbox'
    private String sandbox_name

    VeracodeCreateSandboxTask() {
        group = 'Veracode Sandbox'
        description = "Creates a new sandbox for the given 'app_id'"
        requiredArguments << 'app_id' << 'sandbox_name'
        app_id = project.findProperty("app_id")
        sandbox_name = project.findProperty("sandbox_name")
        defaultOutputFile = new File("${project.buildDir}/veracode", "sandboxinfo-${app_id}-latest.xml")
    }

    void printSandboxInfo(Node xml) {
        XMLIO.getNodeList(xml, 'sandbox').each { sandbox ->
            printf "sandbox_id=%s sandbox_name=\"%s\" owner=%s date=%s\n",
                    XMLIO.getNodeAttributes(sandbox, 'sandbox_id', 'sandbox_name', 'owner', 'created_date')
        }
    }

    void run() {
        Node xml = XMLIO.writeXml(getOutputFile(), veracodeAPI.createSandbox(sandbox_name))
        printSandboxInfo(xml)
        printf "report file: %s\n", getOutputFile()
    }
}