package ru.windcorp.tge2.util.vectors;

public class Vector4<A, B, C, D> {
	
	public A a;
	public B b;
	public C c;
	public D d;
	
	public Vector4(A a, B b, C c, D d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	public A getA() {
		return a;
	}

	public void setA(A a) {
		this.a = a;
	}

	public B getB() {
		return b;
	}

	public void setB(B b) {
		this.b = b;
	}
	
	public C getC() {
		return c;
	}
	
	public void setC(C c) {
		this.c = c;
	}
	
	public D getD() {
		return d;
	}
	
	public void setD(D d) {
		this.d = d;
	}

	@Override
	public String toString() {
		return "Vector4 [a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		result = prime * result + ((c == null) ? 0 : c.hashCode());
		result = prime * result + ((d == null) ? 0 : d.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector4<?, ?, ?, ?> other = (Vector4<?, ?, ?, ?>) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		if (c == null) {
			if (other.c != null)
				return false;
		} else if (!c.equals(other.c))
			return false;
		if (d == null) {
			if (other.d != null)
				return false;
		} else if (!d.equals(other.d))
			return false;
		return true;
	}

}
