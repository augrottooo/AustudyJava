package movie;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Movie[] movies = new Movie[4];
        movies[0] = new Movie(1, "肖申克的救赎", 39.0, 9.7, "弗兰克·德拉邦特", "蒂姆·罗宾斯", "95后");
        movies[1] = new Movie(2, "霸王别姬", 49.0, 9.6, "张国荣", "张丰毅", "90后");
        movies[2] = new Movie(3, "阿甘正传", 25.0, 9.5, "罗伯特·泽米吉斯", "汤姆·汉克斯", "90后");
        movies[3] = new Movie(4, "钢铁侠", 30.0, 9.4, "乔纳森·乔恩斯", "罗伯特·唐尼", "90后");
        MovieOperator operator = new MovieOperator(movies);
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("==电影信息系统==");
            System.out.println("1.查询全部电影详情");
            System.out.println("2.根据电影编号查询电影详情");
            System.out.println("请您输入操作命令:");
            int command = sc.nextInt();
            switch (command) {
                case 1:
                    operator.printAllMovies();
                    break;
                case 2:
                    System.out.println("请输入电影id:");
                    int id = sc.nextInt();
                    operator.searchMovie(id);
                    break;
                default:
                    System.out.println("输入的命令有误");
            }
        }
    }
}
