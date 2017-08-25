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

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradle.util.GFileUtils
import com.veracode.apiwrapper.wrappers.UploadAPIWrapper
import com.veracode.apiwrapper.wrappers.ResultsAPIWrapper

abstract class VeracodeTask extends DefaultTask {
    final static String OPTIONAL = '-optional'
    final static def validArguments = [
            'appId'            : '123',
            'buildId'          : '123',
            'buildName'        : 'xxx',
            'dir'              : 'xxx',
            'force'            : 'force',
            'fileId'           : '123',
            'mode'             : 'action|actionSummary|verbose',
            'maxUploadAttempts': '123',
            'fileId'           : 'xxx',
            'buildId1'         : '123',
            'buildId2'         : '123'
    ]

    def requiredArguments = []

    VeracodeTask() {
        group = 'Veracode'
    }

    abstract void run()

    final String correctUsage() {
        StringBuilder sb = new StringBuilder("Example of usage: gradle ${getName()}")
        requiredArguments.each() {
            if (!isArgumentOptional(it)) {
                sb.append(" -P${it}=${validArguments.get(it)}")
            } else {
                String originalArgument = it.substring(0, it.length() - OPTIONAL.length())
                sb.append(" [-P${originalArgument}=${validArguments.get(originalArgument)}]")
            }
        }

        sb.toString()
    }

    final boolean haveRequiredArguments() {
        boolean haveRequiredArguments = true
        requiredArguments.each() {
            if (!isArgumentOptional(it)) {
                haveRequiredArguments &= getProject().hasProperty(it)
            }
        }

        if (!haveRequiredArguments) {
            println correctUsage()
        }

        return haveRequiredArguments
    }

    @TaskAction
    final def vExecute() { if (haveRequiredArguments()) run() }

    // === utility methods ===
    protected boolean isArgumentOptional(String arg) {
        arg.endsWith(OPTIONAL)
    }

    protected UploadAPIWrapper loginUpdate() {
        UploadAPIWrapper api = new UploadAPIWrapper()
        if (project.veracodeCredentials.apiCredentials) {
            api.setUpApiCredentials(project.veracodeCredentials.id, project.veracodeCredentials.key)
        } else {
            api.setUpCredentials(project.veracodeCredentials.username, project.veracodeCredentials.password)
        }
        return api
    }

    protected ResultsAPIWrapper loginResults() {
        ResultsAPIWrapper api = new ResultsAPIWrapper()
        if (project.veracodeCredentials.apiCredentials) {
            api.setUpApiCredentials(project.veracodeCredentials.id, project.veracodeCredentials.key)
        } else {
            api.setUpCredentials(project.veracodeCredentials.username, project.veracodeCredentials.password)
        }
        return api
    }

    protected Node writeXml(String filename, String content) {
        GFileUtils.writeFile(content, new File(filename))
        new XmlParser().parseText(content)
    }

    protected def readXml(String filename) {
        new XmlParser().parseText(GFileUtils.readFile(new File(filename)))
    }

    protected List readListFromFile(File file) {
        def set = new HashSet<Set>();
        file.eachLine { line ->
            if (set.contains(line)) {
                println "ERROR: duplicate line: [$line]"
            }
            set.add(line)
        }
        return new ArrayList<String>(set)
    }
}