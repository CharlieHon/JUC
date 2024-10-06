package com.charlie.n3;

/**
 * 栈与栈帧：每个线程启动后，虚拟机就会为其分配一块栈内存
 * <lo>
 *     <li>
 *         每个栈由多个栈帧（Frame）组成，对应每次方法时所占的内存，包括局部变量表、返回地址等
 *     </li>
 *     <li>
 *         每个线程只能有一个活动栈帧，对应着当前正在执行的那个方法
 *     </li>
 * </lo>
 */
public class TestFrames {
    public static void main(String[] args) {

        new Thread(() -> method1(20), "t1").start();

        method1(10);
    }

    private static void method1(int x) {
        int y = x + 1;
        Object m = method2();
        System.out.println(m);
    }

    private static Object method2() {
        Object n = new Object();
        return n;
    }
}
