//====================================================================
//  工学部「情報環境実験２」(富永)  C言語プログラミング
//  ポーカーゲームの戦略
//--------------------------------------------------------------------
//  Poker  usr/s16t287_02/  PokerOpe.c
//--------------------------------------------------------------------
//  米谷研究室  s16t287 檜垣大地
//  2018.12.27
//====================================================================


//====================================================================
//  仕様
//====================================================================

/*--------------------------------------------------------------------
---------------------------------------------------------------------*/

//====================================================================
//  前処理
//====================================================================

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#include "Poker.h"

typedef struct{
  int num;
  int sym;
  int originIndex;
}cardInfo;

typedef struct{
  int count;
  int index[5];
}anlzInfo;

//--------------------------------------------------------------------
//  関数宣言
//--------------------------------------------------------------------

void initMyArray(anlzInfo *Array, int size);
void analyzeHand(int *hd, anlzInfo *symIndex, anlzInfo *numIndex, cardInfo *myhd);
int  decideTrash(int lock_length);

//====================================================================
//  戦略
//====================================================================

/*--------------------------------------------------------------------
//  ユーザ指定
//--------------------------------------------------------------------

最初の手札のまま交換しない。

hd : 手札配列
fd : 場札配列(テイク内の捨札)
cg : チェンジ数
tk : テイク数
ud : 捨札配列(過去のテイクも含めた全ての捨札)
us : 捨札数

--------------------------------------------------------------------*/

int strategy(int hd[], int fd[], int cg, int tk, int ud[], int us)
{
  int lock_length = 0;                             // 確定させた手札の長さ
  cardInfo myhd_c[HNUM];                           // コピーした手札
  anlzInfo numIndex[13];                           // 数位的解析情報
  anlzInfo symIndex[4];                            // 記号的解析情報

  srand((unsigned)time(NULL));
  initMyArray(numIndex, 13); initMyArray(symIndex, 4);
  analyzeHand(hd, symIndex, numIndex, myhd_c);

  return decideTrash(lock_length);
}


//====================================================================
//  補助関数
//====================================================================

void initMyArray(anlzInfo *Array, int size){
  int i, j;

  for(i = 0; i < size; i++){
    Array[i].count = 0;
    for (j = 0; j < 5; j++){ (Array[i]).index[j] = -1; }
  }
  return;
}

void analyzeHand(int *hd, anlzInfo *symIndex, anlzInfo *numIndex, cardInfo *myhd){
  int i, tmp;

  for (i = 0; i < HNUM; i++){
    myhd[i].sym = tmp = hd[i] / 13; symIndex[tmp].count++;
    (symIndex[tmp]).index[symIndex[tmp].count] = i;
    myhd[i].num = tmp = hd[i] % 13; numIndex[tmp].count++;
    (numIndex[tmp]).index[numIndex[tmp].count] = i;
    myhd[i].originIndex = i;
  }
  return;
}

int decideTrash(int lock_length){
  return rand() % ((HNUM + 1) - (lock_length)) - 1;
}
