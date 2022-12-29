## 인프런 재고 시스템으로 알아보는 동시성이슈 해결 방법 강의 보고 공부 하였습니다.
## 동시성 문제
-  ProductService 클래스의 테스트 코드에서 multi thread 를 통해서 동시성 테스트 시, 동시성 문제 발생
- decrease 메서드에서 동시에 여러 쓰레드에서 접근 하여 감소 시, 최종적으로 0으로 될것 을 예상 하지만 그렇지 않음, 이유는 여러 쓰레드에서 동일한 id 조회, 후 감소 (각 스레드에서는 다른 스레드에서 변경한 값을 가지지 못하고 변경 전의 값을 가지고 있어서, 그 값을 다시 변경 하는 현상 발생)
- 이런 현상을 Race Condition 이라고 함
  - Race Condition : 두 개 이상의 프로세스가 공통 자원을 병행적으로 읽거나 쓰는 동작을 할 때, 공용 데이터에 대한 접근이 어떤 순서에 따라 이루어졌는지에 따라 실행 결과가 달라지는 현상

## synchronized 키워드
-  multi thread 환경에서 thread-safe를 제공해주기 위해 java 에서 지원해주는 키워드 
- 하지만 @Transaction 애노테이션을 통해서 같이 사용할 경우에는 동시성 문제가 발생할 수 있음
- @Transactional 은 aop를 이용해서 해당 애노테이션이 붙은 클래스나 메소드를 트랜잭션을 이용할 수 있게 해준다. 즉 해당 객체를 프록시 객체로 감싸준다.
  - 프록시 객체에서 트랜잭션 시작 -> target 호출 -> 트랜잭션 커밋 or 롤백 수행
  - 트랜잭션 시작 -> target 호출 후 트랜잭션 커밋 or 롤백 시 다른 스레드에서 해당 target 을 호출 할 수 있는 동시성 문제가 발생
- 해당 문제는 SynchronizedTransactionProductService 클래스에서 해결
  - synchronized 키워드를 통해서 SynchronizedTransactionProductService.decrease 를 동기화를 한 후, ProductService.decrease 호출(트랜잭션 처리)

## TABLE Lock
1. Pessimistic Lock
   - 자원에 대한 동시 요청이 발생하여 일관성에 문제가 생길 것이라고 비관적으로 생각하고 이를 방지 하기 위해 우선 락을 거는 방식
     - 실제 데이터에 Lock을 걸어서 정합성을 맞추는 방법, exclusive lock 을 걸게 되면 다른 트랜잭션에서는 lock에 해제되기 전에 데이터를 가져갈 수 없게 됩니다. 데드락이 걸릴 수 있기 떄문에 주의
       * exclusive lock
          * 트랜잭션에서 데이터를 변경할 때, 해당 트랜잭션이 완료 될 때까지 해당 테이블 또는 레코드를 다른 트랜잭션에서 읽거나 쓰지 못하게 exclusive lock 을 걸고 트랜잭션 진행
          * exclusive lock 에 걸리면 shared lock 을 걸 수 없다 / exclusive lock 걸린 테이블, 레코드는 다른 트랜잭션이 exclusive lock 걸 수 없다
          * select column from table ~ for update
       * shared lock
          * 어떤 트랜잭션에서 데이터를 읽고자 할 때, 다른 shared lock 허락되지만 exclusive lock 은 불가 즉 동시에 읽기는 가능하지만 쓰기는 불가능
          * select column from table ~ for share
      
2. Optimistic Lock
   - 실제로 Lock을 이용하지 않고 버전을 이용함으로써, 정합성을 맞추는 방법, 먼저 데이터를 읽은 후 update 수행 할 때, 현재 버전이 맞는지 확인하여 업데이트, 내가 읽은 버전에서 수정사항이 생겼을 경우에 application 다시 읽은 후 작업 수행
   - jpa 에서는 @Version 어노테이션을 지원
      - @Version 어노테이션을 붙이면 엔티티가 수정될 때 자동으로 버전이 하나씩 증가하며, 수정할 때 조회 시점의 버전과 다를 경우 OptimisticLockException 발생

3. Named Lock
   - 이름을 가진 metadata locking, 이름을 가진 lock 획득 한 후 해제할 때까지 다른 세션은 이 lock 을 획득할 수 없도록 한다. 주의 할 점으로는 transaction 이 종료 될 때, lock이 자동으로 해제되지 않습니다. 별도의 명령어로 해제를 수행해주거나 선점시간이 끝나야 해제
      - GET_LOCK('WORD') : 함수를 통해서 임의의 문자열에 대해 잠금 설정
      - RELEASE_LOCK('WORD') : 임의의 문자열에 대해 획득했던 잠금 해제