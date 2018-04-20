package utility;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DisjointSet<T>
{
    private Map<T, T> parents;
    private Map<T, Integer> depths;
    private Map<T, Set<T>> sets;

    /**
     * Disjoint Set - Constructor
     */
    public DisjointSet()
    {
        this.parents = new HashMap();
        this.depths = new HashMap();
        this.sets = new HashMap<>();
    }

    /**
     * Creates a new set in the disjoint set if not already in a set
     *
     * @param u u new element to add the set
     *
     * @return true if element u is not already not a set, false otherwise
     */
    public boolean createSet(T u)
    {
        if(find(u) == null)
        {
            parents.put(u, u);
            depths.put(u, 0);
            Set<T> newSet = new HashSet<>();
            newSet.add(u);
            sets.put(u, newSet);
            return true;
        }
        else
        {
           return false;
        }
    }

    /**
     * Unions to sets together
     *
     * @param p p element
     * @param q q element
     *
     * @return true if p and q are elements of some set and the sets are not already in the same set, false otherwise
     */
    public boolean union(T p, T q)
    {
        T pid = find(p);
        T qid = find(q);
        if(pid == null || qid == null || pid == qid)
        {
            return false;
        }
        else
        {
            if(depths.get(pid) > depths.get(qid))
            {
                sets.get(pid).addAll(sets.get(qid));
                sets.remove(qid);
                parents.put(qid, pid);
            }
            else
            {
                sets.get(qid).addAll(sets.get(pid));
                sets.remove(pid);
                parents.put(pid, qid);
                if(depths.get(pid).equals(depths.get(qid)))
                {
                    depths.put(qid, depths.get(qid) + 1);
                }
            }
            return true;
        }
    }

    /**
     * Finds the representative set element from element p
     *
     * @param p element in the disjoint set to find the representative set value
     *
     * @return the representative set value if p is not null and is contained in a set, otherwise null
     */
    public T find(T p)
    {
        if(p == null || parents.get(p) == null)
        {
            return null;
        }
        else if(p != parents.get(p))
        {
            parents.put(p, find(parents.get(p)));
        }
        return parents.get(p);
    }

    /**
     * Gets the set that p is contained in
     *
     * @param p element that the set is contained in
     * @return the set that element p is contained if in a set and not null, otherwise null
     */
    public Set<T> getSet(T p)
    {
        T pid = find(p);
        if(pid == null)
        {
            return null;
        }
        else
        {
            return new HashSet<>(sets.get(pid));
        }
    }

    /**
     * Gets the set containing all sets in the disjoint set, if no sets exist,
     * then it returns an empty set
     *
     * @return the set containing all sets in the disjoint set
     */
    public Set<Set<T>> getSets()
    {
        Set<Set<T>> allSets = new HashSet<>();
        for(T key : sets.keySet())
        {
            allSets.add(getSet(key));
        }
        return allSets;
    }
}
