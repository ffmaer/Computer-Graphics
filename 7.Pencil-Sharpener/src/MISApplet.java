/* Ken Perlin
 * Better substrateAs we discussed in class, instead of using BufferedApplet, it is more efficient to write directly to the color framebuffer as an array. The Java library provides a way to do this through its Memory Image Source facility. To get started with this better way of doing things, download the source from the example I showed in class: http://mrl.nyu.edu/~perlin/MIS
 * https://web.archive.org/web/20150918171426/http://mrl.nyu.edu/~perlin/courses/spring2012/0405.html
 */

import java.applet.*;
import java.awt.*;
import java.awt.image.*;

@SuppressWarnings("serial")
public class MISApplet extends Applet implements Runnable {
	public int W, H;

	// YOUR APPLET CAN OVERRIDE THE FOLLOWING TWO METHODS:

	public void initialize() {
	}

	public void initFrame(double time) {
	} // INITIALIZE EACH FRAME

	public void setPixel(int x, int y, int rgb[], int host[],
			boolean super_sampling) {
	} // SET COLOR AT EACH PIXEL

	// INITIALIZE THINGS WHEN APPLET STARTS UP

	public void copy_table() {
		for (int y = 0; y < H; y += nn) {
			for (int x = 0; x < W; x += nn) { // COMPUTE COLOR FOR EACH PIXEL

				table_tmp[y * W + x] = table[y * W + x];

			}
		}
	}

	public void white_screen() {
		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) { // COMPUTE COLOR FOR EACH PIXEL
				pix[y * W + x] = pack(255, 255, 255);
			}
		}
	}

	public void clean_table() {
		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) { // COMPUTE COLOR FOR EACH PIXEL
				table_tmp[y * W + x] = false;
				table[y * W + x] = false;
				hosts_2[y * W + x] = 0;

				// pix_rgb[y*W+x][0]=(float)0.0;
				// pix_rgb[y*W+x][1]=(float)0.0;
				// pix_rgb[y*W+x][2]=(float)0.0;
			}
		}
	}

	public void init() {

		setLayout(new BorderLayout());
		W = getBounds().width; // FIND THE RESOLUTION OF THE JAVA APPLET
		H = getBounds().height;
		pix = new int[W * H]; // ALLOCATE A FRAME BUFFER IMAGE
		pix_rgb = new float[W * H][3];
		table = new boolean[W * H];
		hosts = new int[W * H];
		hosts_2 = new int[W * H];

		table_tmp = new boolean[W * H];

		clean_table();
		mis = new MemoryImageSource(W, H, pix, 0, W);
		mis.setAnimated(true);
		im = createImage(mis); // MAKE MEMORY IMAGE SOURCE FOR FRAME BUFFER

		initialize();

		startTime = clockTime(); // FETCH CLOCK TIME WHEN APPLET STARTS
		new Thread(this).start(); // START THE BACKGROUND RENDERING THREAD
	}

	// UPDATE DISPLAY AT EACH FRAME, BY DRAWING FROM MEMORY IMAGE SOURCE

	public void update(Graphics g) {
		g.drawImage(im, 0, 0, null);
	}

	// BACKGROUND THREAD: COMPUTE AND DRAW FRAME, EVERY 30 MILLISEC

	public void run() {
		while (true) {
			// white_screen();

			if (sampling) {
				computeImage(clockTime() - startTime);
			} else {
				computeImage_default(clockTime() - startTime);
			}
			mis.newPixels(0, 0, W, H, true);
			repaint();
			 try {
			 Thread.sleep(30);
			 } catch (InterruptedException ie) {
			 }
		}
	}

	// COMPUTE IMAGE, GIVEN ANIMATION TIME

	private int rgb[] = new int[3];
	private int host[] = new int[1];

	float[][] pix_rgb;
	boolean[] table;
	int[] hosts;
	int[] hosts_2;

	boolean[] table_tmp;
	boolean super_sampling;
	int nn = 6;
	int count, super_count;
	int h1, h2;
	boolean debug_mode = false;
	boolean sampling = true;

	public void computeImage_default(double time) {
		initFrame(time); // INITIALIZE COMPUTATION FOR FRAME
		super_sampling = false;
		int i = 0;
		for (int y = 0; y < H; y++)
			for (int x = 0; x < W; x++) { // COMPUTE COLOR FOR EACH PIXEL
				setPixel(x, y, rgb, host, super_sampling);
				pix[i++] = pack(rgb[0], rgb[1], rgb[2]);
			}
	}

	public void computeImage(double time) {
		initFrame(time); // INITIALIZE COMPUTATION FOR FRAME

		super_sampling = false;
		count = 0;
		super_count = 0;
		for (int y = 0; y < H; y += nn) {
			for (int x = 0; x < W; x += nn) { // COMPUTE COLOR FOR EACH PIXEL
				setPixel(x + nn / 2, y + nn / 2, rgb, host, super_sampling);
				count++;

				if (debug_mode) {
					if (host[0] == -1) {
						System.out.print("x ");

					} else {
						System.out.print(host[0] + " ");
					}
				}

				// int i=0;
				// int j=0;
				//
				// pix[(y+i)*W+(x+j)] = pack(rgb[0],rgb[1],rgb[2]);
				//
				// for(int k=0;k<3;k++){
				// pix_rgb[(y+i)*W+(x+j)][k] = (float)(rgb[k]/255.0);
				// }
				// hosts[(y+i)*W+(x+j)] = host[0];
				//
				//

				for (int i = 0; i < nn; i++) {
					for (int j = 0; j < nn; j++) {

						if (y + i < H && x + j < W) {
							pix[(y + i) * W + (x + j)] = pack(rgb[0], rgb[1],
									rgb[2]);

							for (int k = 0; k < 3; k++) {
								pix_rgb[(y + i) * W + (x + j)][k] = (float) (rgb[k] / 255.0);
							}
							hosts[(y + i) * W + (x + j)] = host[0];
						}
					}
				}

			}

			if (debug_mode) {
				System.out.print("\n");

			}
		}

		for (int y = 0; y < H; y += nn) {
			for (int x = 0; x < W; x += nn) { // COMPUTE COLOR FOR EACH PIXEL

				c1 = pix_rgb[y * W + x];
				h1 = hosts[y * W + x];

				// horizontal
				if (x + nn < W) {
					c2 = pix_rgb[y * W + (x + nn)];
					h2 = hosts[y * W + (x + nn)];

					if (h2 != h1) {
						super_sampling = true;
						hosts_2[y * W + x] = 8;
						hosts_2[y * W + (x + nn)] = 8;

					} else {
						super_sampling = false;
					}

					if (cie.jnd(c1, c2)) {
						// current
						if (!table[y * W + x]) {
							for (int i = 0; i < nn; i++) {
								for (int j = 0; j < nn; j++) {
									if (y + i < H && x + j < W) {
										// if(!(i==0 && j==0)){
										setPixel(x + j, y + i, rgb, host,
												super_sampling);
										count++;

										if (super_sampling) {
											super_count++;

										}
										pix[(y + i) * W + (x + j)] = pack(
												rgb[0], rgb[1], rgb[2]);
										for (int k = 0; k < 3; k++) {
											pix_rgb[(y + i) * W + (x + j)][k] = (float) (rgb[k] / 255.0);
										}
										// }
									}
								}
							}
							table[y * W + x] = true;
						}

						// right
						if (!table[y * W + (x + nn)]) {
							for (int i = 0; i < nn; i++) {
								for (int j = 0; j < nn; j++) {
									if (y + i < H && (x + nn) + j < W) {
										// if(!(i==0 && j==0)){
										setPixel((x + nn) + j, y + i, rgb,
												host, super_sampling);
										count++;
										if (super_sampling) {
											super_count++;

										}
										pix[(y + i) * W + ((x + nn) + j)] = pack(
												rgb[0], rgb[1], rgb[2]);
										for (int k = 0; k < 3; k++) {
											pix_rgb[(y + i) * W
													+ ((x + nn) + j)][k] = (float) (rgb[k] / 255.0);
										}
										// }
									}
								}
							}
							table[y * W + (x + nn)] = true;
						}

					}
				}

				// vertical

				if (y + nn < H) {
					c2 = pix_rgb[(y + nn) * W + x];
					h2 = hosts[(y + nn) * W + x];

					if (h2 != h1) {
						super_sampling = true;
						hosts_2[(y) * W + x] = 8;
						hosts_2[(y + nn) * W + x] = 8;
					} else {
						super_sampling = false;
					}

					if (cie.jnd(c1, c2)) {

						// current
						if (!table[y * W + x]) {
							for (int i = 0; i < nn; i++) {
								for (int j = 0; j < nn; j++) {
									if (y + i < H && x + j < W) {
										// if(!(i==0 && j==0)){
										setPixel(x + j, y + i, rgb, host,
												super_sampling);
										count++;
										if (super_sampling) {
											super_count++;

										}

										pix[(y + i) * W + (x + j)] = pack(
												rgb[0], rgb[1], rgb[2]);
										for (int k = 0; k < 3; k++) {
											pix_rgb[(y + i) * W + (x + j)][k] = (float) (rgb[k] / 255.0);
										}
										// }
									}
								}
							}
							table[y * W + x] = true;
						}
						// down
						if (!table[(y + nn) * W + x]) {

							for (int i = 0; i < nn; i++) {
								for (int j = 0; j < nn; j++) {
									if ((y + nn) + i < H && x + j < W) {
										// if(!(i==0 && j==0)){
										setPixel(x + j, (y + nn) + i, rgb,
												host, super_sampling);
										count++;
										if (super_sampling) {
											super_count++;

										}

										pix[((y + nn) + i) * W + (x + j)] = pack(
												rgb[0], rgb[1], rgb[2]);
										for (int k = 0; k < 3; k++) {
											pix_rgb[((y + nn) + i) * W
													+ (x + j)][k] = (float) (rgb[k] / 255.0);
										}
										// }
									}
								}
							}

						}

						table[(y + nn) * W + x] = true;
					}

				}

			}

		}

		super_sampling = true;
		for (int y = 0; y < H; y += nn) {
			for (int x = 0; x < W; x += nn) { // COMPUTE COLOR FOR EACH PIXEL

				if (table[(y * W) + x]) {
					for (int p = -1; p < 2; p++) {
						for (int q = -1; q < 2; q++) {
							if (y + (nn * p) < H && (x + (nn * q)) < W
									&& y + (nn * p) > 0 && (x + (nn * q)) > 0) {

								if (!table_tmp[(y + (nn * p)) * W
										+ (x + (nn * q))]) {

									for (int i = 0; i < nn; i++) {
										for (int j = 0; j < nn; j++) {
											if ((y + (nn * p)) + i < H
													&& (x + (nn * q)) + j < W) {
												if (!(i == 0 && j == 0)) {
													hosts_2[(y + (nn * p) + i)
															* W
															+ (x + (nn * q) + j)] = 8;
													super_count++;

													setPixel(x + (nn * q) + j,
															y + (nn * p) + i,
															rgb, host,
															super_sampling);
													count++;

													pix[(y + (nn * p) + i)
															* W
															+ (x + (nn * q) + j)] = pack(
															rgb[0], rgb[1],
															rgb[2]);
													for (int k = 0; k < 3; k++) {
														pix_rgb[(y + (nn * p) + i)
																* W
																+ (x + (nn * q) + j)][k] = (float) (rgb[k] / 255.0);
													}
												}
											}
										}
									}

									table_tmp[(y + (nn * p)) * W
											+ (x + (nn * q))] = true;

								}

							}
						}
					}
				}

			}
		}

		// // grid
		// for(int y = 0; y < H; y+=nn){
		// for(int x = 0; x < W; x+=nn) {
		// pix[y*W+x] = pack(255,255,255);
		// }
		// }

		// super sampling talbe

		if (debug_mode) {
			System.out.println("------");
			for (int y = 0; y < H; y += nn) {
				for (int x = 0; x < W; x += nn) { // COMPUTE COLOR FOR EACH
													// PIXEL
					if (hosts_2[y * W + x] == 0) {
						System.out.print(". ");

					} else {
						System.out.print(hosts_2[y * W + x] + " ");
					}

				}
				System.out.print("\n");
			}

			// table
			System.out.println("count: " + count);
			System.out.println("count/all: " + (double) count
					/ (double) (H * W));

			System.out.println("super count: " + super_count);
			System.out.println("super count/count: " + (double) super_count
					/ (double) count);
		}

		clean_table();

	}

	CIELab cie = new CIELab();
	float[] c1, c2;

	public int pack(int red, int grn, int blu) {
		return 255 << 24 | clip(red, 0, 255) << 16 | clip(grn, 0, 255) << 8
				| clip(blu, 0, 255);
	}

	public int unpack(int packedRGB, int component) {
		return packedRGB >> 8 * (2 - component) & 255;
	}

	public int xy2i(int x, int y) {
		return x + W * y;
	}

	int clip(int t, int lo, int hi) {
		return t < lo ? lo : t > hi ? hi : t;
	}

	// RETURN THE TIME, IN SECONDS, ON THE CLOCK

	double clockTime() {
		return System.currentTimeMillis() / 1000.;
	}

	// PRIVATE DATA

	public int[] pix; // THE FRAME BUFFER ARRAY

	private MemoryImageSource mis; // MEMORY IMAGE SOURCE CONTAINING FRAME
									// BUFFER
	private Image im; // IMAGE CONTAINING THE MEMORY IMAGE SOURCE
	private double startTime; // CLOCK TIME THAT THE APPLET STARTED

}
