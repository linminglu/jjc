	/*
	 * Created on 2005-7-25
	 *
	 * TODO To change the template for this generated file go to
	 * Window - Preferences - Java - Code Style - Code Templates
	 */
	package com.framework.dao.hibernate;
	
	/**
	 * @author zhangkeyi
	 *	ps.getItems()得到已分页好的结果集 
	 *	ps.getIndexes()得到分页索引的数组 
	 *	ps.getTotalCount()得到总结果数 
	 *	ps.getStartIndex()当前分页索引 
	 *	ps.getNextIndex()下一页索引 
	 *	ps.getPreviousIndex()上一页索引
	 * 
	 */
	import java.util.List; 
	
	public class PaginationSupport { 
	
	        public final static int PAGESIZE = 30; 
	
	        private int pageSize = PAGESIZE; 
	
	        private List items; 
	
	        private int totalCount; 
	
	        private int[] indexes = new int[0]; 
	
	        private int startIndex = 0; 
	
	        public PaginationSupport(){
	        	
	        }
	        
	        public PaginationSupport(List items, int totalCount) { 
	                setPageSize(PAGESIZE); 
	                setTotalCount(totalCount); 
	                setItems(items);                
	                setStartIndex(0); 
	        } 
	
	        public PaginationSupport(List items, int totalCount, int startIndex) { 
	                setPageSize(PAGESIZE); 
	                setTotalCount(totalCount); 
	                setItems(items);                
	                setStartIndex(startIndex); 
	        } 
	
	        public PaginationSupport(List items, int totalCount, int pageSize, int startIndex) { 
	                setPageSize(pageSize); 
	                setTotalCount(totalCount); 
	                setItems(items); 
	                setStartIndex(startIndex); 
	        } 
	
	        public List getItems() { 
	                return items; 
	        } 
	
	        public void setItems(List items) { 
	                this.items = items; 
	        } 
	
	        public int getPageSize() { 
	                return pageSize; 
	        } 
	
	        public void setPageSize(int pageSize) { 
	                this.pageSize = pageSize; 
	        } 
	
	        public int getTotalCount() { 
	                return totalCount; 
	        } 
	
	        public void setTotalCount(int totalCount) { 
	                if (totalCount > 0) { 
	                        this.totalCount = totalCount; 
	                        int count = totalCount / pageSize; 
	                        if (totalCount % pageSize > 0) 
	                                count++; 
	                        indexes = new int[count]; 
	                        for (int i = 0; i < count; i++) { 
	                                indexes[i] = pageSize * i; 
	                        } 
	                } else { 
	                        this.totalCount = 0; 
	                } 
	        } 
	
	        public int[] getIndexes() { 
	                return indexes; 
	        } 
	
	        public void setIndexes(int[] indexes) { 
	                this.indexes = indexes; 
	        } 
	
	        public int getStartIndex() { 
	                return startIndex; 
	        } 
	
	        public void setStartIndex(int startIndex) { 
	                if (totalCount <= 0) 
	                        this.startIndex = 0; 
	                else if (startIndex >= totalCount) 
	                        this.startIndex = indexes[indexes.length - 1]; 
	                else if (startIndex < 0) 
	                        this.startIndex = 0; 
	                else { 
	                        this.startIndex = indexes[startIndex / pageSize]; 
	                } 
	        } 
	
	        public int getNextIndex() { 
	                int nextIndex = getStartIndex() + pageSize; 
	                if (nextIndex >= totalCount) 
	                        return getStartIndex(); 
	                else 
	                        return nextIndex; 
	        } 
	
	        public int getPreviousIndex() { 
	                int previousIndex = getStartIndex() - pageSize; 
	                if (previousIndex < 0) 
	                        return 0; 
	                else 
	                        return previousIndex; 
	        } 
	
	}
