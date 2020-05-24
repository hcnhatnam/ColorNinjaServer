/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.entity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author namhcn
 */
public class SlotLock {

    public static SlotLock INSTANCE = new SlotLock();
    private final List<Object> _slotLock = new ArrayList<Object>();
    private final int NSLOTLOCK;

    private SlotLock() {
        NSLOTLOCK = 128;
        for (int i = 0; i < NSLOTLOCK; i++) {
            _slotLock.add(new String("lockObj_" + (i + 1)));
        }
    }

    public Object _getSlotLock(String key) {
        int slot = Math.abs(key.hashCode()) % _slotLock.size();
        return _slotLock.get(slot);
    }

}
