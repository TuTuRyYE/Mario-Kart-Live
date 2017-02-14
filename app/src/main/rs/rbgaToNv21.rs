//File : rgbaToNV21.rs

#pragma version(1)
#pragma rs_fp_relaxed
#pragma rs java_package_name (fr.enseeiht.superjumpingsumokart.arpack)

#define VIDEO_WIDTH 640
#define VIDEO_HEIGHT 480

rs_allocation nv21ByteArray;

int r, g, b, c, l, Y;

void RS_KERNEL convertToNV21(int argb_8888Pixel, uint32_t x){

    c = x % VIDEO_WIDTH;
    l = ((x + 1) / VIDEO_WIDTH);

    r = (argb_8888Pixel & 0xff0000) >> 16;
    g = (argb_8888Pixel & 0xff00) >> 8;
    b = (argb_8888Pixel & 0xff);

    Y = ( (  66 * r + 129 * g +  25 * b + 128) >> 8) +  16;

    rsSetElementAt_uchar(nv21ByteArray, ((char) clamp(Y, 0, 255)), x);

    if ((c % 2) == 0 && (l % 2) == 0) {
        int uvIndex = VIDEO_HEIGHT * VIDEO_WIDTH + (x - ((l / 2) * VIDEO_WIDTH));
        int U = ( ( -38 * r -  74 * g + 112 * b + 128) >> 8) + 128;
        int V = ( ( 112 * r -  94 * g -  18 * b + 128) >> 8) + 128;
        rsSetElementAt_char(nv21ByteArray, ((char) clamp(V, 0, 255)), uvIndex++);
        rsSetElementAt_char(nv21ByteArray, ((char) clamp(U, 0, 255)), uvIndex);
    }

    return;
}