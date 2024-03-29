package com.espica.tools.srtparser;

import android.util.SparseArray;

import java.util.*;

public class SRTInfo  {
    private final SparseArray<SRT> info;
    private final TreeMap<Long,Integer> srtTimeHashMap;

    /**
     * Creates a new instance of SRTInfo.
     */
    public SRTInfo() {
        info = new SparseArray<>();
        srtTimeHashMap = new TreeMap<>();
    }

    /**
     * Creates a new instance of SRTInfo.
     * This constructor acts as a copy constructor.
     *
     * @param srtInfo the SRTInfo object
     */
//    public SRTInfo(SRTInfo srtInfo) {
//        info = new SparseArray<SRT>(srtInfo.info);
//    }

    /**
     * Adds SRT object into SRTInfo object. If SRT object already exists, the old
     * SRT object will be replaced with the new SRT object.
     *
     * @param srt the SRT object to be added
     */
    public void add(SRT srt) {
//        remove(srt);
        info.put(srt.number,srt);
        srtTimeHashMap.put(srt.startTime,srt.number);
    }

    /**
     * {@inheritDoc}
     */
//    public Iterator<SRT> iterator() {
//        return info.iterator();
//    }

    /**
     * Gets the number of SRT objects stored in SRTInfo object.
     *
     * @return the number of SRT objects stored in SRTInfo object
     */
    public int size() {
        return info.size();
    }

    /**
     * Removes the SRT object from SRTInfo.
     *
     * @param srt the SRT object to be removed from SRTInfo
     */
//    public void remove(SRT srt) {
//        // Set.remove() will check if the object is present in the Set, so
//        // there is no need to do another check if the object is present in
//        // the set
//        info.remove(srt);
//    }

    /**
     * Removes the SRT object with subtitle number from SRTInfo.
     *
     * @param number the subtitle number to be removed from SRTInfo
     */
//    public void remove(int number) {
//        info.remove(get(number));
//
//    }

    /**
     * Gets the SRT object from a given number.
     *
     * @param number the subtitle number
     * @return the SRT object
     */
    public SRT get(int number) {
        // Create a dummy SRT object since the comparison is by number only.
        return info.get(number);
    }

    public int getSRTNumber(long time)
    {
        return srtTimeHashMap.floorEntry(time).getValue();
    }

    /**
     * Gets the SRT object.
     *
     * @param srt the SRT object
     * @return the SRT object
     */
//    public SRT get(SRT srt) {
//        return info.tailSet(srt).first();
//    }

    /**
     * Check if the subtitle number is in the SRTInfo object.
     *
     * @param number the subtitle number
     * @return true if the subtitle number is in the SRTInfo; false otherwise
     */
//    public boolean contains(int number) {
//        return info.contains(new SRT(number, 0));
//    }

    /**
     * Check if the SRT is in the SRTInfo object.
     *
     * @param srt the SRT object
     * @return true if the subtitle number is in the SRTInfo; false otherwise
     */
//    public boolean contains(SRT srt) {
//        return info.contains(srt);
//    }

    /**
     * {@inheritDoc}
     */
//    @Override
//    public Object clone() {
//        return new SRTInfo(this);
//    }
}
