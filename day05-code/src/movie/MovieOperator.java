package movie;

public class MovieOperator {
    private Movie[] movies;
    public MovieOperator(Movie[] movies) {
        this.movies = movies;
    }

    public void printAllMovies() {
        System.out.println("----所有电影信息如下----");
        for (int i = 0; i < movies.length; i++) {
            Movie m = movies[i];
            System.out.println("编号："+m.getId());
            System.out.println("名称："+m.getName());
            System.out.println("价格："+m.getPrice());
            System.out.println("---------------------");
        }
    }

    public void searchMovie(int id) {
        for (int i = 0; i < movies.length; i++) {
            Movie m = movies[i];
            if (m.getId() == id) {
                System.out.println("----电影详情如下----");
                System.out.println("编号："+m.getId());
                System.out.println("名称："+m.getName());
                System.out.println("价格："+m.getPrice());
                System.out.println("评分："+m.getScore());
                System.out.println("导演："+m.getDirector());
                System.out.println("主演："+m.getActor());
                System.out.println("其他信息："+m.getInfo());
                System.out.println("------------------");
                return;
            }
        }
        System.out.println("未找到该电影");
    }



}
