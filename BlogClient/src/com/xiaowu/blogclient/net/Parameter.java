package com.xiaowu.blogclient.net;

import java.io.Serializable;

/**
 * 
 * @author wwj
 * 
 */
public class Parameter implements Serializable, Comparable<Parameter> {

	private static final long serialVersionUID = 2721340807561333705L;
	private String name; // 参数名
	private String value; // 参数值

	public Parameter() {
		super();
	}

	public Parameter(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

	@Override
	public int compareTo(Parameter another) {
		return 0;
	}

}
