package repo.Entities;

public abstract class Entity implements Comparable<Entity> {
    @Override
    public int compareTo(Entity o) {
        return this.toString().compareTo(o.toString());
    }
}
