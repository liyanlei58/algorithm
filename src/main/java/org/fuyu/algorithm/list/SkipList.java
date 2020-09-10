package org.fuyu.algorithm.list;

import java.util.Arrays;
import java.util.Random;

/**
 * 跳表
 */
public class SkipList<K extends Comparable<K>, V> {

    private Node<K, V> head;//k,v 都是 NULL
    private Integer levels = 0;
    private Integer length = 0;
    private Random random = new Random(System.currentTimeMillis());

    public SkipList() {
        createNewLevel();
    }

    private Node<K, V> findNode(K key) {
        Node<K, V> curNode = this.head;
        for (; ; ) {
            //curNode.next.key <= key, 继续往后找
            while (curNode.getNext() != null && curNode.getNext().getKey().compareTo(key) <= 0) {
                curNode = curNode.getNext();
            }
            //走到下一层，直到第1层且curNode.next.key >= key为止
            if (curNode.getDown() != null) {
                curNode = curNode.getDown();
            } else {
                break;
            }
        }
        return curNode;
    }

    public V get(K key) {
        Node<K, V> node = findNode(key);
        if (key.equals(node.getKey())) {
            return node.getValue();
        }
        return null;
    }

    public void put(K key, V value) {
        if (key == null || value == null) {
            return;
        }
        Node<K, V> newNode = new Node<>(key, value);
        insertNode(newNode);
    }

    private void insertNode(Node<K, V> newNode) {
        Node<K, V> curNode = findNode(newNode.getKey());

        //相等则更新，否则插入到下一个节点
        if (curNode.getKey() == null) {
            insertNext(curNode, newNode);
        } else if (curNode.getKey().compareTo(newNode.getKey()) == 0) { //update
            curNode.setValue(newNode.getValue());
            return;
        } else {
            insertNext(curNode, newNode);
        }

        int currentLevel = 1;
        Node<K, V> oldTop = newNode;
        while (random.nextInt(100) < 50) {
            Node<K, V> newTop = new Node<>(newNode.getKey(), null);

            if (currentLevel >= levels) {
                createNewLevel();
            }

            //往前找到第一个 up != null
            while (curNode.getPre() != null && curNode.getUp() == null) {
                curNode = curNode.getPre();
            }

            if (curNode.getUp() == null) {
                continue;
            }

            //到了上一层
            curNode = curNode.getUp();

            //curNode->newTop，newTop插入curNode后
            Node<K, V> curNodeNext = curNode.getNext();
            if(curNodeNext != null){
                curNodeNext.setPre(newTop);
            }
            newTop.setNext(curNodeNext);
            curNode.setNext(newTop);
            newTop.setPre(curNode);

            //newTop->oldTop, newTop插入oldTop上面
            newTop.setDown(oldTop);
            oldTop.setUp(newTop);

            //继续往上层找
            oldTop = newTop;
            currentLevel++;
        }
    }

    private void createNewLevel() {
        Node<K, V> newHead = new Node<>(null, null);
        if (this.head == null) {
            this.head = newHead;
            this.levels++;
            return;
        }

        this.head.setUp(newHead);
        newHead.setDown(this.head);
        this.head = newHead;
        this.levels++;
    }

    private void insertNext(Node<K, V> curNode, Node<K, V> newNode) {
        Node<K, V> curNodeNext = curNode.getNext();
        newNode.setNext(curNodeNext);
        if (curNodeNext != null) {
            curNodeNext.setPre(newNode);
        }
        curNode.setNext(newNode);
        newNode.setPre(curNode);
        this.length++;
    }

    public void print() {
        Node<K, V> curI = this.head;

        //length + 1:每层有一个头结点，
        String[][] strings = new String[levels][length + 1];
        for (String[] string : strings) {
            //二维数组，所有元素默认全是0
            Arrays.fill(string, "0");
        }

        // 走到第一层的第一个
        while (curI.getDown() != null) {
            curI = curI.getDown();
        }
        System.out.println("levels:" + levels + "_" + "length:" + length);

        //外层循环→：遍历第一层，内层循环↑：往上找每一列 ↑
        int i = 0;
        while (curI != null) {
            Node<K, V> curJ = curI;

            int j = levels - 1;
            while (curJ != null) {
                //从下往上，写入对应的数值
                strings[j][i] = String.valueOf(curJ.getKey());
                if (curJ.getUp() == null) {
                    break;
                }
                //内层循环↑
                curJ = curJ.getUp();
                j--;
            }
            if (curI.getNext() == null) {
                break;
            }
            //外层循环 →
            curI = curI.getNext();
            i++;
        }

        //遍历输入二维数组的值
        for (String[] string : strings) {
            System.out.println(Arrays.toString(string));
        }

    }

    static final class Node<K extends Comparable<K>, V> {
        private K key;
        private V value;
        private Node<K, V> up, down, pre, next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getUp() {
            return up;
        }

        public void setUp(Node<K, V> up) {
            this.up = up;
        }

        public Node<K, V> getDown() {
            return down;
        }

        public void setDown(Node<K, V> down) {
            this.down = down;
        }

        public Node<K, V> getPre() {
            return pre;
        }

        public void setPre(Node<K, V> pre) {
            this.pre = pre;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }

        @Override
        public String toString() {
            return "Node{" + "key=" + key +
                    ", value=" + value +
                    ", hashcode=" + hashCode() +
                    ", up=" + (up == null ? "null" : up.hashCode()) +
                    ", down=" + (down == null ? "null" : down.hashCode()) + ", pre=" + (pre == null ? "null" : pre.hashCode()) +
                    ", next=" + (next == null ? "null" : next.hashCode()) +
                    '}';
        }
    }

}