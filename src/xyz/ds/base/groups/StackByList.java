package xyz.ds.base.groups;

import java.util.Iterator;

public class StackByList<Item> implements Iterable<Item> {
	private class StackByListIterator implements Iterator<Item>{
		private Node current = first ;
		public boolean hasNext() { return current != null ; }
		public void remove() { }
		public Item next(){
			Item item = current.item ;
			current = current.next ;
			return item ;
		}
	}
	private class Node{
		Item item ;
		Node next ;
	}
	
	private Node first ;
	private int N ;
	
	public void push(Item item){
		Node oldFirst = first ;
		first = new Node() ;
		first.item = item ;
		first.next = oldFirst ;
		N ++ ;
	}
	public Item pop(){
		Item item = first.item ;
		first = first.next ;
		N -- ;
		return item ;
	}
	
	@Override
	public Iterator<Item> iterator() {
		return new StackByListIterator() ;
	}

	public boolean isEmpty() { return first == null ; }
	public int size() { return N ; }
}
