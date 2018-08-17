/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.example;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.sweble.wikitext.engine.CompiledPage;
import org.sweble.wikitext.engine.Compiler;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.utils.SimpleWikiConfiguration;

public class App
{
	public static final int wrapCol = 80;
	
	public static void main(String[] args) throws Exception
	{
		if (args.length < 1)
		{
			System.err.println("Usage: java -jar swc-example-xpath-archetype-VERSION.jar TITLE QUERY");
			System.err.println();
			System.err.println("  The program will look for a file called `TITLE.wikitext',");
			System.err.println("  parse the file and perform the given QUERY against the document.");
			System.err.println("  The result is written as Wikitext to the file `TITLE.result'.");
			return;
		}
		
		int i = 0;
		
		String fileTitle = args[i++];
		
		String query = args[i++];
		
		String result = run(
				new File(fileTitle + ".wikitext"),
				fileTitle,
				query);
		
		FileUtils.writeStringToFile(new File(fileTitle + ".result"), result);
	}
	
	static String run(File file, String fileTitle, String query) throws Exception
	{
		// Set-up a simple wiki configuration
		SimpleWikiConfiguration config = new SimpleWikiConfiguration(
				"classpath:/org/sweble/wikitext/engine/SimpleWikiConfiguration.xml");
		
		// Instantiate a compiler for wiki pages
		Compiler compiler = new Compiler(config);
		
		// Retrieve a page
		PageTitle pageTitle = PageTitle.make(config, fileTitle);
		
		PageId pageId = new PageId(pageTitle, -1);
		
		String wikitext = FileUtils.readFileToString(file);
		
		// Compile the retrieved page
		CompiledPage cp = compiler.postprocess(pageId, wikitext, null);
		
		return XPath.query(cp, query);
	}
}
