package edu.rpi.legup.utility;

import java.util.*;

public class DisjointSets<T>
{
    private Map<T, T> parents;
    private Map<T, Integer> depths;
    private Map<T, Set<T>> sets;

    /**
     * DisjointSets Constructor - creates an empty DisjointSets
     */
    public DisjointSets()
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
     * @return representative set element or null if the specified element is null or is not in the DisjointSets
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
     * @param p set one
     * @param q set two
     * @return returns true if sets are non-null and disjoint, false otherwise
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
                parents.put(qid, pid);
                sets.get(pid).addAll(sets.get(qid));
                sets.remove(qid);
            }
            else
            {
                parents.put(pid, qid);
                sets.get(qid).addAll(sets.get(pid));
                sets.remove(pid);
                if(depths.get(pid) == depths.get(qid))
                {
                    depths.put(qid, depths.get(qid) + 1);
                }
            }
            return true;
        }
    }

    /**
     * Unions to elements together, if either element is not already in the DisjointSets, it creates a set for the
     * element then unions the sets together. If either element is null, no action is taken.
     *
     * @param p element one
     * @param q element two
     */
    public void addAndUnion(T p, T q)
    {
        if(p != null && q != null)
        {
            T pid = find(p);
            if(pid == null)
            {
                createSet(p);
            }
            T qid = find(q);
            if(qid == null)
            {
                createSet(q);
            }
            union(p, q);
        }
    }

    /**
     * Determines whether the specified element is in the DisjointSets
     *
     * @param u element to check
     * @return true if the DisjointSets contains the specified element, false otherwise
     */
    public boolean contains(T u)
    {
        return parents.containsKey(u);
    }

    /**
     * Gets the set of elements that the specified element is contained in, or null if no such set exists.
     *
     * @param p element to get the set of
     * @return the set of elements that the specified element if contained in, or null if no such set exists
     */
    public Set<T> getSet(T p)
    {
        T pid = find(p);
        if(pid != null)
        {
            return new HashSet<>(sets.get(pid));
        }
        else
        {
            return null;
        }
    }

    /**
     * Gets a list of all of the sets in the DisjointSets
     *
     * @return list of the sets in the DisjointSets
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
     * Gets the number of disjoint sets
     *
     * @return the number of disjoint sets
     */
    public int setCount()
    {
        return sets.size();
    }

    /**
     * Gets the total number of elements among all sets in the DisjointSets
     *
     * @return the number of elements in the DisjointSets
     */
    public int size()
    {
        return parents.size();
    }
}
