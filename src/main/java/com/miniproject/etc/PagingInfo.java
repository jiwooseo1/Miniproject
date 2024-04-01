package com.miniproject.etc;




public class PagingInfo {
	// 1페이지당 출력할 데이터를 끊어내기 위해 필요한 멤버들
	private int totalPostCnt; // 전체 게시판 글의 갯수
	private int viewPostCntPerPage = 3; // 한페이지당 보여줄 글의 갯수
	private int totalPageCnt; // 총 페이지 수
	private int startRowIndex; // 보여주기 시작할 글의 row index번호
	private int pageNo; // 유저가 클릭한 현재 페이지 번호
	
	// pageNation을 위한 작업
	private int pageCntPerBlock = 2; // 한개 블럭에 보여줄 페이지 갯수
	private int totalPagingBlockCnt; // 전체 페이징 블럭 갯수
	private int pageBlockOfCurrentPage; // 현재 페이지가 속한 페이징 블럭 번호
	private int startNumOfCurrentPagingBlock; // 현재 페이징 블럭에서의 출력 시작 페이지 번호
	private int endNumOfCurrentPagingBlock; // 현재 페이징 블럭에서의 출력 끝 페이지 번호
	
	
	public void setTotalPostCnt(int totalPostCnt) {
		this.totalPostCnt = totalPostCnt;
	}
	
	public int getTotalPostCnt() {
		return this.totalPostCnt;
	}
	
	public void setViewPostCntPerPage(int viewPostCntPerPage) {
		this.viewPostCntPerPage = viewPostCntPerPage;
	}
	
	public int getViewPostCntPerPage() {
		return this.viewPostCntPerPage;
	}
	
	public void setTotalPageCnt(int totalPostCnt, int viewPostCntPerPage) {
		// 총 페이지 수 = 글의 총 갯수 / 한페이지당 보여줄 글의 갯수 => 나누어 떨어지지 않으면 + 1
		
		if (totalPostCnt % viewPostCntPerPage == 0) {
			this.totalPageCnt = totalPostCnt / viewPostCntPerPage;			
		} else {
			this.totalPageCnt = (totalPostCnt / viewPostCntPerPage) + 1;
		}

	}
	
	public int getTotalPageCnt() {
		return this.totalPageCnt;
	}
	
	
	public void setStartRowIndex() {
		// (현재 페이지번호 - 1) * 페이지당 보여줄 글의 갯수
		this.startRowIndex = (this.pageNo -1) * this.viewPostCntPerPage;
	}

	
	public int getStartRowIndex() {
		return this.startRowIndex;
	}

	
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	// ---- 페이지 블럭 관련 변수 getter, setter
	
	public void setTotalPagingBlockCnt() {
		// 전체 페이지 수 / pateCntPerBlask -- > 나누어 떨어지지 않으면 +1 
		if ((this.totalPageCnt % this.pageCntPerBlock) == 0) {
			this.totalPagingBlockCnt = this.totalPageCnt / this.pageCntPerBlock;
		} else {
			this.totalPagingBlockCnt = (this.totalPageCnt / this.pageCntPerBlock) + 1;
		}
		
	}
	public int getTotalPagingBlockCnt() {
		return this.totalPagingBlockCnt;
	}
	
	public void setPageBlockofCurrentPage() {
		// 현재 페이지가 속한 페이징 블럭 번호
		// 현재페이지 번호 / pageCntPerBlock -> 나누어 떨어지지않으면 올림
		if ((this.pageNo % this.pageCntPerBlock) == 0) {
			this.pageBlockOfCurrentPage = this.pageNo / this.pageCntPerBlock;
		} else {
			this.pageBlockOfCurrentPage = (int)Math.ceil(this.pageNo / (double)this.pageCntPerBlock);  
		}
		
	}
	
	public int getPageBlockofCurrentPage() {
		return this.pageBlockOfCurrentPage;
	}
	
	public void setStartNumOfCurrentPagingBlock() {
		// 현재 페이징 블럭에서의 출력 시작 페이지 번호
		// (블럭 번호 - 1 ) * pageCntPerBlock + 1
		this.startNumOfCurrentPagingBlock = (this.pageBlockOfCurrentPage -1) * this.pageCntPerBlock + 1;
		
	}
	
	public int getStartNumOfCurrentPagingBlock() {
		return this.startNumOfCurrentPagingBlock;
	}
	
	public void setEndNumOfCurrentPagingBlock() {
		// 현재 페이징 블럭에서의 출력 끝 페이지 번호
		// 블럭 번호 * pageCntPerBlock
		this.endNumOfCurrentPagingBlock = this.pageBlockOfCurrentPage * this.pageCntPerBlock;
		
		if (this.endNumOfCurrentPagingBlock > this.totalPageCnt) {
			this.endNumOfCurrentPagingBlock = this.totalPageCnt;
		}
		
	}
	
	public int getEndNumOfCurrentPagingBlock() {
		return this.endNumOfCurrentPagingBlock;
	}

	
	public int getPageCntPerBlock() {
		return pageCntPerBlock;
	}

	public void setPageCntPerBlock(int pageCntPerBlock) {
		this.pageCntPerBlock = pageCntPerBlock;
	}

	@Override
	public String toString() {
		return "PagingInfo [totalPostCnt=" + totalPostCnt + ", viewPostCntPerPage=" + viewPostCntPerPage + ", totalPageCnt=" + totalPageCnt
				+ ", startRowIndex=" + startRowIndex + ", pageNo=" + pageNo + ", pageCntPerBlock=" + pageCntPerBlock + ", totalPagingBlockCnt="
				+ totalPagingBlockCnt + ", pageBlockOfCurrentPage=" + pageBlockOfCurrentPage + ", startNumOfCurrentPagingBlock="
				+ startNumOfCurrentPagingBlock + ", endNumOfCurrentPagingBlock=" + endNumOfCurrentPagingBlock + "]";
	}
	
	
}
