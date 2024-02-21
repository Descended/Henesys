package henesys.client.anticheat;

public class Rand32 {

   private long seed1;
   private long seed2;
   private long seed3;

   public Rand32(int s1, int s2, int s3) {
      this.seed1 = s1 | 0x100000;

      this.seed2 = s2 | 0x1000;

      this.seed3 = s3 | 0x10;
   }

   public long random() {
      long v8 = (seed1 << 12) ^ (seed1 >>> 19) ^ ((seed1 >>> 6) ^ (seed1 << 12)) & 0x1FFF;
      long v9 = 16 * seed2 ^ (seed2 >>> 25) ^ ((16 * seed2) ^ (seed2 >>> 23)) & 0x7F;
      long v10 = (seed3 >>> 11) ^ (seed3 << 17) ^ ((seed3 >>> 8) ^ (seed3 << 17)) & 0x1FFFFF;

      seed1 = v8 & 0xFFFFFFFFL;
      seed2 = v9 & 0xFFFFFFFFL;
      seed3 = v10 & 0xFFFFFFFFL;
      return (v8 ^ v9 ^ v10) & 0xFFFFFFFFL;
   }

   public long[] getRandom(int amount) {
      long[] rand = new long[amount];
      for (int i = 0; i < rand.length; ++i) {
         rand[i] = this.random();
      }
      return rand;
   }
}
