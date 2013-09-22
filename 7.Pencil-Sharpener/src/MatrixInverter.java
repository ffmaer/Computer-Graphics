//<pre> INVERT A TRANSLATION/ROTATION/SCALE MATRIX -Ken Perlin

public class MatrixInverter
{
   public static void invert(double src[][], double dst[][]) {

      // COMPUTE ADJOINT COFACTOR MATRIX FOR THE ROTATION/SCALE 3x3 SUBMATRIX

      for (int i = 0 ; i < 3 ; i++)
      for (int j = 0 ; j < 3 ; j++) {
         int iu = (i + 1) % 3, iv = (i + 2) % 3;
         int ju = (j + 1) % 3, jv = (j + 2) % 3;
         dst[j][i] = src[iu][ju] * src[iv][jv] - src[iu][jv] * src[iv][ju];
      }

      // RENORMALIZE BY DETERMINANT TO INVERT ROTATION/SCALE SUBMATRIX

      double det = src[0][0]*dst[0][0] + src[1][0]*dst[0][1] + src[2][0]*dst[0][2];
      for (int i = 0 ; i < 3 ; i++)
      for (int j = 0 ; j < 3 ; j++)
         dst[i][j] /= det;

      // INVERT TRANSLATION

      for (int i = 0 ; i < 3 ; i++)
         dst[i][3] = -dst[i][0]*src[0][3] - dst[i][1]*src[1][3] - dst[i][2]*src[2][3];

      // NO PERSPECTIVE

      for (int i = 0 ; i < 4 ; i++)
         dst[3][i] = i < 3 ? 0 : 1;
   }
} 
