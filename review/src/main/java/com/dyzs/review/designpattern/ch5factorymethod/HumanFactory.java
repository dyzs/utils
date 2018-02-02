package com.dyzs.review.designpattern.ch5factorymethod;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @author maidou, created on 2018/2/2.
 * 工厂方法模式还有一个非常重要的应用，就是延迟始化(Lazy initialization)，什么是延迟始化呢？
 * 一个对象初始化完毕后就不释放，等到再次用到得就不用再次初始化了，直接从内存过中拿到就可以了
 * 怎么实现呢，很简单，看例子：加入一个 map
 *
 * 这个在类初始化很消耗资源的情况比较实用，或者是为了初始化一个类需要准备比较多条件（参数），通过这种方式可以很好的减少项目的复杂程度
 */
@SuppressWarnings("all")
public class HumanFactory {
    //定义一个MAP,初始化过的Human对象都放在这里
    private static HashMap<String, Human> humans = new HashMap<String, Human>();



    public static Human createHuman(Class clazz) {
        Human human = null;
        try {
            //如果MAP中有，则直接从取出，不用初始化了
            if(humans.containsKey(clazz.getSimpleName())){
                human = humans.get(clazz.getSimpleName());
            }else{
                human = (Human) Class.forName(clazz.getName()).newInstance();
                //放到MAP中
                humans.put(clazz.getSimpleName(), human);
            }
        } catch (InstantiationException e) {
            System.out.println("必须指定人种的颜色");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println("人种定义错误！");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("混蛋，你指定的人种找不到！");
            e.printStackTrace();
        } finally {
            return human;
        }
    }

    //女娲生气了，把一团泥巴塞到八卦炉，哎产生啥人种就啥人种
    public static Human createHuman(){
        Human human = null; //定义一个类型的人类
        //首先是获得有多少个实现类，多少个人种
        List<Class> concreteHumanList = ClassUtils.getAllClassByInterface(Human.class); //定义了多少人种
        //八卦炉自己开始想烧出什么人就什么人
        Random random = new Random();
        int rand = random.nextInt(concreteHumanList.size());
        human = createHuman(concreteHumanList.get(rand));
        return human;
    }
}
