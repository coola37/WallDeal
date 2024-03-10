#pragma version(1)
#pragma rs_fp_relaxed

rs_allocation inputImage;
rs_allocation outputImage;

int width;
int height;
float radius;

void root(const uchar4 *v_in, uchar4 *v_out, const void *usrData, uint32_t x, uint32_t y) {
    float4 sum = 0;
    int count = 0;

    for (int i = -2; i <= 2; i++) {
        for (int j = -2; j <= 2; j++) {
            if ((x + i >= 0) && (x + i < width) && (y + j >= 0) && (y + j < height)) {
                sum += rsUnpackColor8888(rsGetElementAt_uchar4(inputImage, x + i, y + j));
                count++;
            }
        }
    }

    sum /= count;
    *v_out = rsPackColorTo8888(sum);
}
