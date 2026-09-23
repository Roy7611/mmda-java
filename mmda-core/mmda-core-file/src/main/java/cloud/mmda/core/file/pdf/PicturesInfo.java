package cloud.mmda.core.file.pdf;

import java.io.Serializable;

/**
 * 图片信息
 */
public class PicturesInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 最小行
     */
    private int minRow;

    /**
     * 最大行
     */
    private int maxRow;

    /**
     * 最小列
     */
    private int minCol;

    /**
     * 最大列
     */
    private int maxCol;

    private int dx1;

    private int dx2;

    private int dy1;

    private int dy2;

    /**
     * 扩展
     */
    private String ext;

    /**
     * 图片数据
     */
    private byte[] pictureData;

    public int getDx1() {
        return dx1;
    }

    public PicturesInfo setDx1(int dx1) {
        this.dx1 = dx1;
        return this;
    }

    public int getDx2() {
        return dx2;
    }

    public PicturesInfo setDx2(int dx2) {
        this.dx2 = dx2;
        return this;
    }

    public int getDy1() {
        return dy1;
    }

    public PicturesInfo setDy1(int dy1) {
        this.dy1 = dy1;
        return this;
    }

    public int getDy2() {
        return dy2;
    }

    public PicturesInfo setDy2(int dy2) {
        this.dy2 = dy2;
        return this;
    }

    public int getMinRow() {
        return minRow;
    }

    public PicturesInfo setMinRow(int minRow) {
        this.minRow = minRow;
        return this;
    }

    public int getMaxRow() {
        return maxRow;
    }

    public PicturesInfo setMaxRow(int maxRow) {
        this.maxRow = maxRow;
        return this;
    }

    public int getMinCol() {
        return minCol;
    }

    public PicturesInfo setMinCol(int minCol) {
        this.minCol = minCol;
        return this;
    }

    public int getMaxCol() {
        return maxCol;
    }

    public PicturesInfo setMaxCol(int maxCol) {
        this.maxCol = maxCol;
        return this;
    }

    public String getExt() {
        return ext;
    }

    public PicturesInfo setExt(String ext) {
        this.ext = ext;
        return this;
    }

    public byte[] getPictureData() {
        return pictureData;
    }

    public PicturesInfo setPictureData(byte[] pictureData) {
        this.pictureData = pictureData;
        return this;
    }
}
