package eu.heliosteam.heliosumlgen.javaModel;

public class Relation {
    public final AbstractJavaThing base;
    public final AbstractJavaThing other;

    public Relation(AbstractJavaThing base, AbstractJavaThing other) {
        this.base = base;
        this.other = other;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((base == null) ? 0 : base.hashCode());
        result = prime * result + ((other == null) ? 0 : other.hashCode());
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
        Relation other = (Relation) obj;
        if (base == null) {
            if (other.base != null)
                return false;
        } else if (!base.equals(other.base))
            return false;
        if (this.other == null) {
            return other.other == null;
        } else return this.other.equals(other.other);
    }
}
