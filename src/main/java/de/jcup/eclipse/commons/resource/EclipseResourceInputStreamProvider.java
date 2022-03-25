package de.jcup.eclipse.commons.resource;
/*
 * Copyright 2018 Albert Tregnaghi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 */


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class EclipseResourceInputStreamProvider implements ResourceInputStreamProvider{

	private String rootPath;
	
	public EclipseResourceInputStreamProvider(String pluginId){
		this.rootPath="platform:/plugin/"+pluginId;
	}
	
	@Override
	public InputStream getStreamFor(String path) throws IOException{
		URL url = null;
		try{
			url = new URL(rootPath+path);
		}catch(MalformedURLException e){
			return null;
		}
		return url.openConnection().getInputStream();
	}
}
