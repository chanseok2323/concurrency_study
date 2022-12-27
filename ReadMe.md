1. Pessimistic Lock
    - 실제 데이터에 Lock을 걸어서 정합성을 맞추는 방법, exclusive lock 을 걸게 되면 다른 트랜잭션에서는 lock에 해제되기 전에 데이터를 가져갈 수 없게 됩니다. 데드락이 걸릴 수 있기 떄문에 주의
2. Optimistic Lock
    - 실제로 Lock을 이용하지 않고 버전을 이용함으로써, 정합성을 맞추는 방법, 먼저 데이터를 읽은 후 update 수행 할 때, 현재 버전이 맞는지 확인하여 업데이트, 내가 읽은 버전에서 수정사항이 생겼을 경우에 application 다시 읽은 후 작업 수행
3. Named Lock
    - 이름을 가진 metadata locking, 이름을 가진 lock 획득 한 후 해제할 때까지 다른 세션은 이 lock 을 획득할 수 없도록 한다. 주의 할 점으로는 transaction 이 종료 될 때, lock이 자동으로 해제되지 않습니다. 별도의 명령어로 해제를 수행해주거나 선점시간이 끝나야 해제