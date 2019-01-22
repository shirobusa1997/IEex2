//====================================================================
//  工学部「情報環境実験２」(富永)  C言語プログラミング
//  ポーカーゲームの戦略
//--------------------------------------------------------------------
//  Poker  usr/s16t287_02/  PokerOpe.c
//  実行環境 1)
//    Apple macOS 10.14.1 Mojave g++(Apple LLVM 10.0.0, clang-1000.11.45.5)
//  実行環境 2)
//    Microsoft Windows10 gcc XXX
//  実行環境 3)
//    UbuntuLinux 14.04.5LTS gcc 4.8.4(Ubuntu 4.8.4-2ubuntu1~14.04.4)
//    (GoogleCloudPlatform VMインスタンスにて環境構築)
//--------------------------------------------------------------------
//  米谷研究室  s16t287 檜垣大地
//  2018.12.XX
//====================================================================


//====================================================================
//  仕様
//====================================================================

/*--------------------------------------------------------------------

手札、場札、チェンジ数、テイク数、捨札を引数とし、捨札を決める。
返却値は、捨札の位置である。-1のときは、交換しないで、手札を確定させる。
関数 strategy() は、戦略のインタフェースであり、この関数内で、
実際の戦略となる関数を呼び出す。手札と捨札は、不正防止のため、
変更不可なので、局所配列にコピーして、整列などの処理を行う。
複数の戦略を比較したり、パラメタを変化させて、より強い戦略を目指す。

---------------------------------------------------------------------*/


//====================================================================
//  前処理
//====================================================================

// 標準ライブラリを指定します。
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>

// 事前に定義された、ポーカーゲーム用ヘッダを指定します。
#include "Poker.h"

// 手札配列のサイズ(ルールより5のみ有効)
#define MYCARD_SIZE 5

// 手札のポイントマクロ
#define  NP   0   // ノーペア
#define  OP   1   // ワンペア
#define  TP   2   // ツーペア
#define  TK   8   // スリーカインズ(スリーカード)
#define  ST  32   // ストレート
#define  FL  24   // フラッシュ
#define  FH  16   // フルハウス
#define  FC  64   // フォーカード
#define  SF 128   // ストレートフラッシュ
#define RSF 256   // ロイヤルストレートフラッシュ

// カードの記号をT_Symbol型として列挙します。
typedef enum {S, H, D, C} T_Symbol;
// カード情報をT_Info型構造体として宣言します。
typedef struct {
  T_Symbol symbol;
  int number;
  int original_index;
} T_Info;
// カード統計情報を格納するT_ANLZInfo型構造体として宣言します。
typedef struct {
  int count;
  int index[5];
} T_ANLZInfo;

//====================================================================
//  USE FOR DEBUG ONLY
//====================================================================

// #define F_DEBUG

// [DEBUG]デバッグ時専用事前処理
/*
#ifdef F_DEBUG
  // [DEBUG]カード記号名
  const char *symbol_disp[] = {"SPADE   ", "HEART   ", "DIAMOND ", "CLUB    "};
#endif
*/

//--------------------------------------------------------------------
//  関数宣言
//--------------------------------------------------------------------



//====================================================================
//  戦略
//====================================================================

/*--------------------------------------------------------------------
//  ユーザ指定
//--------------------------------------------------------------------

指定された引数のフォーマットと、それらが有する意味は、以下の通りである。

hd : 手札配列
fd : 場札配列(テイク内の捨札)
cg : チェンジ数
tk : テイク数
ud : 捨札配列(過去のテイクも含めた全ての捨札)
us : 捨札数

--------------------------------------------------------------------*/

// T_ANLZInfo構造体を用いた、配列タイプを含む変数の初期化を行う関数initialize_T_ANLZInfoです。
void initialize_T_ANLZInfo(T_ANLZInfo *array, int size){
  // メンバ宣言
  int i, j;                                  // テンポラリ変数

  // 配列を含む、参照指定された変数を初期化します。
  for(i = 0; i < size; i++){
    array[i].count = 0;
    for(j = 0; j < 5; j++){ array[i].index[j] = 0;}
  }
  return;
}

// 手札情報を解析し、保持する関数analyze_cardInfoです。
void analyze_cardInfo(int *myhd, T_Info *t_i, T_ANLZInfo *numIndex, T_ANLZInfo *symIndex){
  // メンバ宣言
  int i;                                      // カウンタ変数
  int tmp;                                    // テンポラリ変数

  for(i = 0; i < MYCARD_SIZE; i++){
    t_i[i].symbol = tmp = myhd[i] / 13; (symIndex[tmp]).index[symIndex[tmp].count] = i;
    symIndex[tmp].count++;
    t_i[i].number = tmp = myhd[i] % 13; (numIndex[tmp]).index[numIndex[tmp].count] = i;
    numIndex[tmp].count++;
    t_i[i].original_index = i;
  }
}

// 1~3ペアを重視した、役の期待と捨て札判定をする関数judgeFCです。
int judgePair(T_Info *t_i, T_Info *hd_res, T_ANLZInfo *numIndex){
  // メンバ宣言
  int i = 0;                              // カウンタ変数
  int pair = 0;                           // 検出されたペア数
  int length = 0;                         // 捨てるべきカードの数
  int trash[HNUM] = {0};                  // 捨てるべきカードの添字

  // 成立しているペアの確認
  for (i = 0; i < 13; i++) {
    if (numIndex[i].count >= 2) { pair++; }
  }
  // 2ペアである場合、余りのカードを捨てるカードの候補とする
  if (pair == 2) {
      for (i = 0; i < 13; i++) {
        if(numIndex[i].count == 1) { return (numIndex[i]).index[0]; }
      }
  }
  // 捨て札の決定
  for (i = 0; i < 13; i++) {
    if (numIndex[i].count == 1) { trash[length] = (numIndex[i]).index[0]; length++; }
  }
  if (length > 0) {
    return trash[rand() % length];
  } else { return -1; }
}

// フラッシュを重視した、役の期待と捨て札判定をする関数judgeFCです。
int judgeFrash(T_Info *t_i, T_ANLZInfo *symIndex){
  // メンバ宣言
  int i, j;                               // カウンタ変数
  int target_symbol;                      // フラッシュを望める記号
  int length = 0;                         // 捨てるべきカードの数
  int trash[HNUM] = {0};                  // 捨てるべきカードの添字

  for (i = 0; i < 4; i++) {
    // すでに手札上でフラッシュが成立していた場合、パスします。
    if (symIndex[i].count == 5) { return -1; }
    else if (symIndex[i].count >= 3) {
      target_symbol = i;
      for (j = 0; j <HNUM; j++) {
        if (t_i[j].symbol != target_symbol) { trash[length] = j; length++; }
      } 
      if (length > 0) { return trash[rand() % length]; }
    }
  }

  // 指定条件を満たせなかった場合、フラッシュは見込めないものとみなします。
  return -10;
}

// フォーカードを重視した、役の期待と捨て札判定をする関数judgeFCです。
int judgeFC(T_Info *t_i, T_ANLZInfo *numIndex) {
  int i, j;                               // カウンタ変数
  int length = 0;                         // 捨てるべきカードの数
  int trash[HNUM] = {0};                  // 捨てるべきカードの添字

  for (i = 0; i < 13; i++) {
    if (numIndex[i].count == 4) { return -1; }
    else if (numIndex[i].count == 3) {
      for (j = 0; j < 13; j++) {
        if (numIndex[i].count == 1) { trash[length] = (numIndex[i]).index[0]; length++; }
      }
      if (length > 0) { return trash[rand() % length]; } 
    }
  }

  // 指定条件を満たせなかった場合、フォーカードは見込めないものとみなします。
  return -10;
}

// ストレート成立が期待できる場合に、手札から捨てるべきカードを選択する、関数searchTrashです。
int searchTrash(T_Info *t_i, T_ANLZInfo *numIndex, int s_combo, int e_combo) {
  int i, j;                               // カウンタ変数
  int length = 0;                         // 捨てるべきカードの数
  int trash[HNUM] = {0};                  // 捨てるべきカードの添字

  for (i = 0; i < s_combo; i++) {
    if ((numIndex[i]).count >= 1) {
      for (j = 1; j < (numIndex[i]).count; j++) {
        trash[length] = (numIndex[i]).index[j];
        length++;
      }
    }
  }

  for (i = s_combo; i <= e_combo; i++) {
    if ((numIndex[i]).count > 1) {
      for (j = 1; j < (numIndex[i]).count; j++) {
        trash[length] = (numIndex[i]).index[j];
        length++;
      }
    }
  }

  for (i = e_combo + 1; i < 13; i++) {
    if ((numIndex[i]).count >= 1) {
      for (j = 1; j < (numIndex[i]).count; j++) {
        trash[length] = (numIndex[i]).index[j];
        length++;
      }
    }
  }

  if (length > 0) { return trash[rand() % length]; }

  return -10;
}

// ストレートを重視した、役の期待と捨て札判定をする関数judgeFCです。
int judgeST(T_Info *t_i, T_ANLZInfo *numIndex) {
  int i;                                // カウンタ変数
  int s_combo, e_combo;                 // カードの連続開始地点と終了地点    
  int length = 0;                       // 連続しているカードの数
  int m_length = 0;                     // カードの最大連続数

  // 数列的にカードが連続する部分を探し、最大連続数を求めます。
  for (i = 0; i < 13; i++){
    if ((numIndex[i]).count > 0) {
      if (length == 0) { s_combo = i; }
      length++;
    } 
    // すでにストレートが成立している場合、パスします。
    else if (length == 5){ return -1; }
    // 現時点までの連続数が、探索全体で最大であった場合、最大連続数を更新します。
    else if (length > m_length){ m_length = length; e_combo = i - 1; length = 0; }
  }

  // ストレート成立が見込める場合、連続列から外れたカードから、捨てるカードを選択します。
  if (m_length >= 3) { return searchTrash(t_i, numIndex, s_combo, e_combo); }

  // 指定条件を満たせなかった場合、ストレートは見込めないものとみなします。
  return -10;
}

int decide_trash_hand(int *myhd, int *fd, T_Info *t_i, T_Info *hd_res, T_ANLZInfo *symIndex, T_ANLZInfo *numIndex){
  // メンバ変数宣言
  int token_pair  = judgePair(t_i, hd_res, numIndex);   // ペア重視の捨て札
  int token_flash = judgeFrash(t_i, symIndex);          // フラッシュ重視の捨て札
  int token_four  = judgeFC(t_i, numIndex);             // フォーカード重視の捨て札
  int token_straight = judgeST(t_i, numIndex);          // ストレート重視の捨て札

  // 優先順にトークンを比較し、正しく捨て札が指定されたものを、最終的な捨て札とします。
  // フォーカード判定が満たされた場合
  if (token_four >= -1) { return token_four; }

  // ストレートフラッシュが見込めるか否か
  if (token_straight == token_flash && token_flash >= -1) { return token_flash; }

  // ストレート判定が満たされた場合
  if (token_straight >= -1) { return token_straight; }
  // フラッシュ判定が満たされた場合
  
  if (token_flash >= -1) { return token_flash; }
  // 上位判定が満たされなかった場合
  else { return token_pair; }
}

int strategy(int hd[], int fd[], int cg, int tk, int ud[], int us)
{
  // メンバ変数宣言
  int i;
  int myhd[MYCARD_SIZE];
  int return_hand = -1;                      // 捨てるカード番号(-1は捨てない)
  T_Info t_i[MYCARD_SIZE];                   // 手札解析情報を付与した手札配列
  T_Info hd_res[MYCARD_SIZE];                // ペアロック後の手札配列
  T_ANLZInfo numIndex_c[13];                 // 数値の重複数(1 ~ 13)
  T_ANLZInfo symIndex_c[4];                  // 記号の重複数(SPADE, HEART, DIAMOND, CLUB)

  // 初期化
  for (i = 0; i < MYCARD_SIZE; i++){ myhd[i] = hd[i]; }
  initialize_T_ANLZInfo(numIndex_c, 13); initialize_T_ANLZInfo(symIndex_c, 4);

  // 乱数生成設定(シード値は現在時刻)
  srand((unsigned)time(NULL));

  // 手札解析
  analyze_cardInfo(myhd, t_i, numIndex_c, symIndex_c);

  // 捨て札判定
  // すでに手札がフォーカード以上の役で構成されている場合、以降は打ち切りとする。
  if (poker_point(myhd) > FC){ return -1; }

  // 捨てるカードの選択
  return_hand = decide_trash_hand(myhd, fd, t_i, hd_res, symIndex_c, numIndex_c);

  return return_hand;
}


//====================================================================
//  補助関数
//====================================================================
