/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package org.appcelerator.kroll.runtime.rhino;

import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollObject;
import org.appcelerator.kroll.runtime.rhino.Proxy.RhinoObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

/**
 * An implementation of KrollFunction for Rhino
 */
public class RhinoFunction implements KrollFunction
{
	private Function fn;

	public RhinoFunction(Function fn)
	{
		this.fn = fn;
	}

	@Override
	public Object call(KrollObject thisObject, Object[] args)
	{
		RhinoObject rhinoObject = (RhinoObject) thisObject;
		Scriptable thisObj = (Scriptable) rhinoObject.getNativeObject();

		Context context = Context.enter();
		context.setOptimizationLevel(-1);
		try {
			for (int i = 0; i < args.length; i++) {
				args[i] = TypeConverter.javaObjectToJsObject(args[i], thisObj);
			}
			return fn.call(context, fn.getParentScope(), thisObj, args);
		} finally {
			Context.exit();
		}
	}

}
