package com.equidais.mybeacon.controller.main;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by empirestate on 1/29/16.
 */
public class GymVisits implements List<GymVisits>{

    public String TIME_ID;
    public String TIME_OUT;
    public String ID;

    public GymVisits() {

    }


    public GymVisits(String ID, String TIME_ID, String TIME_OUT) {
        this.ID = ID;
        this.TIME_ID = TIME_ID;
        this.TIME_OUT = TIME_OUT;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTIME_ID() {
        return TIME_ID;
    }

    public void setTIME_ID(String TIME_ID) {
        this.TIME_ID = TIME_ID;
    }

    public String getTIME_OUT() {
        return TIME_OUT;
    }

    public void setTIME_OUT(String TIME_OUT) {
        this.TIME_OUT = TIME_OUT;
    }

    @Override
    public void add(int location, GymVisits object) {

    }

    @Override
    public boolean add(GymVisits object) {
        return false;
    }

    @Override
    public boolean addAll(int location, Collection<? extends GymVisits> collection) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends GymVisits> collection) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean contains(Object object) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return false;
    }

    @Override
    public GymVisits get(int location) {
        return null;
    }

    @Override
    public int indexOf(Object object) {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @NonNull
    @Override
    public Iterator<GymVisits> iterator() {
        return null;
    }

    @Override
    public int lastIndexOf(Object object) {
        return 0;
    }

    @Override
    public ListIterator<GymVisits> listIterator() {
        return null;
    }

    @NonNull
    @Override
    public ListIterator<GymVisits> listIterator(int location) {
        return null;
    }

    @Override
    public GymVisits remove(int location) {
        return null;
    }

    @Override
    public boolean remove(Object object) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return false;
    }

    @Override
    public GymVisits set(int location, GymVisits object) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @NonNull
    @Override
    public List<GymVisits> subList(int start, int end) {
        return null;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NonNull
    @Override
    public <T> T[] toArray(T[] array) {
        return null;
    }
}
