package eu.heliosteam.heliosumlgen.asm;

public class QualifiedMethod {
	public final String methodName;
	public final String methodDesc;

	public QualifiedMethod(String methodName, String methodArgs) {
		this.methodName = methodName;
		this.methodDesc = methodArgs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((methodDesc == null) ? 0 : methodDesc.hashCode());
		result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
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
		QualifiedMethod other = (QualifiedMethod) obj;
		if (methodDesc == null) {
			if (other.methodDesc != null)
				return false;
		} else if (!methodDesc.equals(other.methodDesc))
			return false;
		if (methodName == null) {
			return other.methodName == null;
		} else return methodName.equals(other.methodName);
	}
	
}
