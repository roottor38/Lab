package test;

import java.util.Arrays;

import org.bitbucket.eunjeon.seunjeon.Analyzer;
import org.bitbucket.eunjeon.seunjeon.CompressedAnalyzer;
import org.bitbucket.eunjeon.seunjeon.Eojeol;
import org.bitbucket.eunjeon.seunjeon.LNode;

public class Sample {
	public static void main(String[] args) {
        // 형태소 분석
        for (LNode node : Analyzer.parseJava("아버지가방에들어가신다.")) {
            System.out.println(node);
//          // morpheme :형태소
//          // surface : 단어
//          // feature : 품사
//          // System.out.println(node.morpheme().getFeatureHead()); // 대표 품사 태그
//          // 품사 태그 : https://docs.google.com/spreadsheets/d/1-9blXKjtjeKZqsf4NzHeYJCrr49-nXeRF6D80udfcwY/edit#gid=589544265
        }
//        LNode(아버지/WrappedArray(N),0,3,-1135)
//        LNode(가/WrappedArray(J),3,4,-738)
//        LNode(방/WrappedArray(N),4,5,660)
//        LNode(에/WrappedArray(J),5,6,203)
//        LNode(들어가/WrappedArray(V),6,9,583)
//        LNode(신다/WrappedArray(EP, E),9,11,-1256)
//        LNode(./WrappedArray(S),11,12,325)
        // 어절 분석
        for (Eojeol eojeol: Analyzer.parseEojeolJava("아버지가방에들어가신다.")) {
            System.out.println(eojeol);
            for (LNode node: eojeol.nodesJava()) {
                System.out.println(node);
            }
        }
//        Eojeol(아버지가,0,4,MutableList(LNode(아버지/WrappedArray(N),0,3,-1135), LNode(가/WrappedArray(J),3,4,-738)))
//        LNode(아버지/WrappedArray(N),0,3,-1135)
//        LNode(가/WrappedArray(J),3,4,-738)
//        Eojeol(방에,4,6,MutableList(LNode(방/WrappedArray(N),4,5,660), LNode(에/WrappedArray(J),5,6,203)))
//        LNode(방/WrappedArray(N),4,5,660)
//        LNode(에/WrappedArray(J),5,6,203)
//        Eojeol(들어가신다,6,11,MutableList(LNode(들어가/WrappedArray(V),6,9,583), LNode(신다/WrappedArray(EP, E),9,11,-1256)))
//        LNode(들어가/WrappedArray(V),6,9,583)
//        LNode(신다/WrappedArray(EP, E),9,11,-1256)
//        Eojeol(.,11,12,MutableList(LNode(./WrappedArray(S),11,12,325)))
//        LNode(./WrappedArray(S),11,12,325)
        /**
         * 사용자 사전 추가
         * surface,cost
         *   surface: 단어명. '+' 로 복합명사를 구성할 수 있다.
         *           '+'문자 자체를 사전에 등록하기 위해서는 '\+'로 입력. 예를 들어 'C\+\+'
         *   cost: 단어 출연 비용. 작을수록 출연할 확률이 높다.
         */
        Analyzer.setUserDict(Arrays.asList("덕후", "버카충,-100", "낄끼+빠빠,-100").iterator());
        for (LNode node : Analyzer.parseJava("덕후냄새가 난다.")) {
            System.out.println(node);
        }
//        LNode(덕후/WrappedArray(N),0,2,-1135)
//        LNode(냄새/WrappedArray(N),2,4,-139)
//        LNode(가/WrappedArray(J),4,5,-437)
//        LNode(난다/WrappedArray(V, E),6,8,348)
//        LNode(./WrappedArray(S),8,9,-394)
        // 활용어 원형
        for (LNode node : Analyzer.parseJava("빨라짐")) {
            for (LNode node2: node.deInflectJava()) {
                System.out.println(node2);
            }
        }
//        LNode(빠르/WrappedArray(V),0,2,-1092)
//        LNode(지/WrappedArray(V),2,3,-1092)
//        LNode(ᄆ/WrappedArray(E),2,3,-1092)
        // 복합명사 분해
        for (LNode node : Analyzer.parseJava("낄끼빠빠")) {
            System.out.println(node);   // 낄끼빠빠
            for (LNode node2: node.deCompoundJava()) {
                System.out.println(node2);  // 낄끼+빠빠
            }
        }
//      LNode(낄끼빠빠/WrappedArray(N),0,4,-1135)
//      LNode(낄끼/WrappedArray(N),0,2,-1135)
//      LNode(빠빠/WrappedArray(N),2,4,-1135)
        // 압축모드 분석(heap memory 사용 최소화. 속도는 상대적으로 느림. -Xmx512m 이하 추천)
        for (LNode node : CompressedAnalyzer.parseJava("아버지가방에들어가신다.")) {
            System.out.println(node);
        }
//        LNode(아버지/WrappedArray(N),0,3,-1135)
//        LNode(가/WrappedArray(J),3,4,-738)
//        LNode(방/WrappedArray(N),4,5,660)
//        LNode(에/WrappedArray(J),5,6,203)
//        LNode(들어가/WrappedArray(V),6,9,583)
//        LNode(신다/WrappedArray(EP, E),9,11,-1256)
//        LNode(./WrappedArray(S),11,12,325)
	}
}
