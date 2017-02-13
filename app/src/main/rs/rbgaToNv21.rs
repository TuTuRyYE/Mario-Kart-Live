//File : rgbaToNV21.rs

#pragma version(1)
#pragma rs_fp_relaxed
#pragma rs java_package_name (fr.enseeiht.superjumpingsumokart.arpack)

#define VIDEO_WIDTH 640
#define VIDEO_HEIGHT 480

rs_allocation nv21ByteArray;

void RS_KERNEL convertToNV21(uchar4 argb_8888Pixel, uint32_t x, uint32_t y){

    int yIndex = y * VIDEO_HEIGHT + x;
    int Y = ( (  66 * argb_8888Pixel.s1 + 129 * argb_8888Pixel.s2 +  25 * argb_8888Pixel.s3 + 128) >> 8) +  16;

    rsSetElementAt_uchar(nv21ByteArray, ((uchar) clamp(Y, 0, 255)), yIndex);

    if (y % 2 == 1 && x % 2 == 1) {
        int uvIndex = VIDEO_HEIGHT * VIDEO_WIDTH + (y / 2) * (VIDEO_WIDTH / 2) + (x / 2);
        int U = ( ( -38 * argb_8888Pixel.s1 -  74 * argb_8888Pixel.s2 + 112 * argb_8888Pixel.s3 + 128) >> 8) + 128;
        int V = ( ( 112 * argb_8888Pixel.s1 -  94 * argb_8888Pixel.s2 -  18 * argb_8888Pixel.s3 + 128) >> 8) + 128;
        rsSetElementAt_uchar(nv21ByteArray, ((uchar) clamp(U, 0, 255)), uvIndex + 1);
        rsSetElementAt_uchar(nv21ByteArray, ((uchar) clamp(V, 0, 255)), uvIndex);
    }

    return;
}