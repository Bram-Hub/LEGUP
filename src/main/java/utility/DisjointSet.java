package utility;

import java.util.*;

public class DisjointSet<T>
{
    private Map<T, T> parents;
    private Map<T, Integer> depths;
    private Map<T, Set<T>> sets;

    /**
     * DisjointSet Constructor - creates an empty DisjointSet
     */
    public DisjointSet()
    {
        this.parents = new HashMap<>();
        this.depths = new HashMap<>();
        this.sets = new HashMap<>();
    }

    /**
     * Creates a unique set that contains the specified element. If the specified element is null or another set already
     * contains that element, this method returns false, indicating that a set was not created
     *
     * @param u element to create the set from
     * @return true if the set was created, false otherwise
     */
    public boolean createSet(T u)
    {
        if(u == null || parents.containsKey(u))
        {
            return false;
        }
        else
        {
            parents.put(u, u);
            depths.put(u, 0);
            Set<T> newSet = new HashSet<>();
            newSet.add(u);
            sets.put(u, newSet);
            return true;
        }
    }

    /**
     * Finds and returns the representative set element of the set that the specified element contains
     *
     * @param p element of the set of which to find
     * @return representative set element or null if the specified element is null
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
     * Unions two sets together. If the set are non-null and disjoint, then it returns true, false otherwise
     *
     * @param u set one
     * @param q set two
     * @return returns true if sets are non-null and disjoint, false otherwise
     */
    public boolean union(T u, T q)
    {
        T pid = find(u);
        T qid = find(q);
        if(pid == null || qid == null || pid == qid)
        {
            return false;
        }
        else
        {
            if(depths.get(pid) > depths.get(qid))
            {
                parents.put(qid, pid);
                sets.get(qid).addAll(sets.get(pid));
                sets.remove(pid);
            }
            else
            {
                parents.put(pid, qid);
                sets.get(pid).addAll(sets.get(qid));
                sets.remove(qid);
                if(depths.get(pid) == depths.get(qid))
                {
                    depths.put(qid, depths.get(qid) + 1);
                }
            }
        }
        return true;
    }

    /**
     * Determines whether the specified element is in the DisjointSet
     *
     * @param u element to check
     * @return true if the DisjointSet contains the specified element, false otherwise
     */
    public boolean contains(T u)
    {
        return parents.containsKey(u);
    }

    /**
     * Gets the set of elements that the specified element is contained in, or null if no such set exists.
     *
     * @param u element to get the set of
     * @return the set of elements that the specified element if contained in, or null if no such set exists
     */
    public Set<T> getSet(T u)
    {
        if(find(u) != null)
        {
            return new HashSet<>(sets.get(u));
        }
        else
        {
            return null;
        }
    }

    /**
     * Gets a list of all of the sets in the DisjointSet
     *
     * @return list of the sets in the DisjointSet
     */
    public List<Set<T>> getAllSets()
    {
        ArrayList<Set<T>> list = new ArrayList<>();
        for(T e : sets.keySet())
        {
            list.add(new HashSet<>(sets.get(e)));
        }
        return list;
    }

    /**
     * Returns the total number of elements among all sets in the DisjointSet
     *
     * @return the number of elements in the DisjointSet
     */
    public int size()
    {
        return parents.size();
    }
}
