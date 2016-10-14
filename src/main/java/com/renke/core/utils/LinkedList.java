package com.renke.core.utils;

/**
 * 链表类
 * 
 * @author JackDou
 * @since 2013-08-11
 */
public class LinkedList {
	
	/** 默认容量大小及链表容量满的时候每次自动增加的大小 */
	private static final int DEFAULT_CAPACITY_SIZE = 12;
	
	/** 链表中的节点集合 */
	protected Node[] nodes;
	
	/** 指针，指向链表末端待添加的位置，默认指向0的位置 */
	private int endPos = 0;
	
	/** 当前链表容量 */
	private int capacity = 0;

	public LinkedList() {
		nodes = new Node[DEFAULT_CAPACITY_SIZE];
		capacity = DEFAULT_CAPACITY_SIZE;
	}
	
	public LinkedList(int capacity) {
		nodes = new Node[capacity];
		this.capacity = capacity;
	}
	
	/**
	 * 链表大小
	 * 
	 * @return 链表大小
	 */
	public int size() {
		return endPos;
	}
	
	public int capacity() {
		return this.nodes.length;
	}
	
	/**
	 * 向链表中加入一个节点
	 * 
	 * @param node 新加入的节点
	 */
	public void add(Node node) {
		if (this.endPos >= this.capacity() - 1) {
			capacity = this.endPos + 1 + DEFAULT_CAPACITY_SIZE;
			Node[] nodesTemp = new Node[capacity];
			System.arraycopy(nodes, 0, nodesTemp, 0, this.endPos);
			nodes = nodesTemp;
		}
		if (endPos == 0) {
			node.setIndex(0);
			nodes[endPos] = node;
		} else if (endPos > 0 && endPos <= this.capacity() - 1) {
			node.setIndex(endPos);
			nodes[endPos] = node;
			nodes[endPos-1].setNext(nodes[endPos]);
		}
		endPos++;
	}
	
	/**
	 * 删除链表元素
	 * 
	 * @param index 要删除链表中节点的位置
	 */
	public void remove(int index) {
		if (index > this.capacity() - 1 || index >= this.endPos || index < 0) {
			throw new RuntimeException("没有位置索引" + index + "存在");
		}
		if (index == endPos-1) {
			nodes[index] = null;
			return;
		}
		for (; 0 <= index && index < endPos-1; index++) {
			if (index == 0) {
				nodes[index] = nodes[index+1];
				nodes[index].setIndex(index);
			} else {
				nodes[index] = nodes[index+1];
				nodes[index].setIndex(index);
				nodes[index-1].setNext(nodes[index]);
			}
		}
		endPos--;
	}
	
	/**
	 * 更新指定链表位置节点的值
	 * 
	 * @param index 指定的链表位置
	 * @param value 指点链表位置上的节点的值
	 */
	public void update(int index, Object value) {
		if (index > this.capacity() - 1 || index >= this.endPos || index < 0) {
			throw new RuntimeException("没有位置索引" + index + "存在");
		}
		nodes[index].setValue(value);
	}
	
	/**
	 * 获取指点链表位置上的节点的值
	 * 
	 * @param index 指定的链表位置
	 * @return 指点链表位置上的节点
	 */
	public Node getNodeByValue(Object value) {
		for (Node node : this.nodes) {
			if(node != null && node.getValue().equals(value)) {
				return node;
			}
		}
		
		return null;
	}
	
	/**
	 * 获取指点链表位置上的节点的值
	 * 
	 * @param index 指定的链表位置
	 * @return 指点链表位置上的节点
	 */
	public Node getNode(int index) {
		if (index > this.capacity() - 1 || index >= this.endPos || index < 0) {
			throw new RuntimeException("没有位置索引" + index + "存在");
		}
		return nodes[index];
	}
	
	/**
	 * 在链表指定位置插入节点
	 * 
	 * @param index 指定的链表位置
	 * @param value 节点
	 */
	public void insert(int index, Node node) {
		if (index > this.capacity() - 1 || index >= this.endPos || index < 0) {
			throw new RuntimeException("没有位置索引" + index + "存在");
		}
		if (this.endPos >= this.capacity() - 1) {
			capacity = this.endPos + 1 + DEFAULT_CAPACITY_SIZE;
			Node[] nodesTemp = new Node[capacity];
			System.arraycopy(nodes, 0, nodesTemp, 0, this.endPos);
			nodes = nodesTemp;
		}
		Node[] nodesTemp = new Node[size()- index];
		System.arraycopy(nodes, index, nodesTemp, 0, nodesTemp.length);

		for (int i = 0, len= nodesTemp.length; i< len; i++) {
			nodesTemp[i].setIndex(nodesTemp[i].getIndex() + 1);
		}
		endPos++;
		node.setIndex(index);
		System.arraycopy(nodesTemp, 0, nodes, index+1, nodesTemp.length);
			node.setNext(nodes[0]);
		nodes[index] = node;
		nodes[index-1].setNext(nodes[index]);
		nodes[index].setNext(nodes[index+1]);
	}
	
	/**
	 * 获取链表的第一个节点
	 * 
	 * @return 链表的第一个节点
	 */
	public Node getFirst() {
		return nodes[0];
	}
	
	/**
	 * 获取链表的最后一个节点
	 * 
	 * @return 链表的最后一个节点
	 */
	public Node getLast() {
		return nodes[endPos-1];
	}
	
	/**
	 * 链表反序
	 */
	public LinkedList reverse() {
		Node[] nodesTemp = new Node[capacity];
		for (int i = 0, j = endPos - 1; i < endPos &&  j >= 0; i++, j--) {
			nodesTemp[i] = nodes[j];
			nodesTemp[i].setIndex(i);
			if (j > 0) {
				nodesTemp[i].setNext(nodes[j-1]);
			}
		}
		nodes = nodesTemp;
		return this;
	}
	
	public String toString() {
		StringBuilder strBui = new StringBuilder();
		for (int  i = 0; i < endPos; i++) {
			strBui.append(nodes[i].toString());
			if (i < endPos) {
				strBui.append(",");
			}
		}
		return strBui.toString();
	}

	/**
	 * 获取当前链表容量
	 * 
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}
	
	public static void main(String[] args) {
		LinkedList linkedList = new LinkedList();
		for (int i = 0; i < 15; i++) {
			linkedList.add(new Node(i));
		}
		
		System.out.println(linkedList);
		
		Node first = linkedList.getFirst();
		while(first.getNext() != null) {
			System.out.println(first.getNext() + ",value:" + first.getValue());
			first = first.getNext();
		}
		
		Node nodeByValue = linkedList.getNodeByValue(3);
		System.out.println(nodeByValue);
	}
}
