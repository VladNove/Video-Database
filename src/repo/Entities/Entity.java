package repo.Entities;

public abstract class Entity implements Comparable<Entity> {
    @Override
    public final int compareTo(final Entity o) {
        return this.toString().compareTo(o.toString());
    }
}
