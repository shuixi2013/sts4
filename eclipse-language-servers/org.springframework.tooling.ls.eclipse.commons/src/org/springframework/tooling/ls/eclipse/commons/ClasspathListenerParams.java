/*******************************************************************************
 * Copyright (c) 2018 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.tooling.ls.eclipse.commons;

public class ClasspathListenerParams {

	private String callbackCommandId;

	public ClasspathListenerParams(String callbackCommandId) {
		super();
		this.callbackCommandId = callbackCommandId;
	}

	public String getCallbackCommandId() {
		return callbackCommandId;
	}

	public void setCallbackCommandId(String callbackCommandId) {
		this.callbackCommandId = callbackCommandId;
	}

	@Override
	public String toString() {
		return "ClasspathListenerParams [callbackCommandId=" + callbackCommandId + "]";
	}

}
