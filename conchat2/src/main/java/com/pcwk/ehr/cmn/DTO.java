package com.pcwk.ehr.cmn;

public class DTO {

	private long no; // 글 번호
	private long totalCnt; // 총 글 수
	private long pageSize; // 10,20,30,50,100,...
	private long pageNo; // 1,2,3,...

	private String searchDiv; // 검색 구분
	private String searchWord; // 검색 구분

	public DTO() {
	}

	public long getNo() {
		return no;
	}

	public void setNo(long no) {
		this.no = no;
	}

	public long getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(long tatalCnt) {
		this.totalCnt = tatalCnt;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public long getPageNo() {
		return pageNo;
	}

	public void setPageNo(long pageNo) {
		this.pageNo = pageNo;
	}

	public String getSearchDiv() {
		return searchDiv;
	}

	public void setSearchDiv(String searchDiv) {
		this.searchDiv = searchDiv;
	}

	public String getSearchWord() {
		return searchWord;
	}

	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}

	@Override
	public String toString() {
		return "DTO [no=" + no + ", totalCnt=" + totalCnt + ", pageSize=" + pageSize + ", pageNo=" + pageNo
				+ ", searchDiv=" + searchDiv + ", searchWord=" + searchWord + "]";
	}

}
