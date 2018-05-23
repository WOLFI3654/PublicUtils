package de.wolfi.utils;

public abstract class Runable<T> implements java.lang.Runnable {
	private T a;

	public Runable(T superer) {

		this.a = superer;
	}

	public T a() {
		return this.a;
	}

	public void a(T a) {
		this.a = a;
	}

}
