public interface ManagementInterface {
	public void compact();
	public int[] allocateDataBlock(int numberOfBlocks);
	public boolean freeDataBlocks(int[] blockId);
	public void format();
	public String getDataBlockInfo(int blockId);
	public int[] getEmptyFileBlockList();
	public int[] getUsedFileBlockList();
	public boolean saveToFile(String fileName);
}