package org.fuyu.algorithm.list;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * 跳表测试类
 */
public class SkipListTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void skipList() {
        SkipList<Integer, String> skipList = new SkipList<>();
        skipList.put(2, "B");
        skipList.put(1, "A");
        skipList.put(3, "C");
        skipList.put(4, "D");
        skipList.put(5, "E");
        skipList.put(6, "F");
        skipList.print();
        System.out.println(skipList.get(2));
    }
}
