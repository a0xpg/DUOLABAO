package com.hualong.duolabao.dao;

/**
 * Created by Administrator on 2019-07-28.
 */
public class testBubbleSort {

    public static final int SIZE=10;
    //冒泡排序算法
    public static void bubbleSort(int[] a){
        System.out.println("第步的排序结果为："+a.length);

        int temp;
        for(int i=1;i<a.length;i++){
            for(int j=0;j<a.length-i;j++){
                if(a[j]>a[j+1]){
                    temp = a[j];
                    a[j] = a[j+1];
                    a[j+1] = temp;
                }
            }
            //输出每步排序后的结果
            System.out.print("第"+i+"步的排序结果为：");
            for(int j=0;j<a.length;j++){
                System.out.print(" "+a[j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int[] a = new int[SIZE];
        //为数组元素赋值
        for(int i=0;i<a.length;i++){
            a[i] = (int) (100 + Math.random()*100);
        }
        //输出排序前的数组元素
        System.out.println("排序前的数组为：");
        for(int i=0;i<a.length;i++){
            System.out.print(a[i]+" ");
        }
        System.out.print("\n");
        //对数组排序
        bubbleSort(a);
        System.out.println("排序后的数组为：");
        for(int i=0;i<a.length;i++){
            System.out.print(a[i]+" ");
        }
        System.out.print("\n");
    }
}
