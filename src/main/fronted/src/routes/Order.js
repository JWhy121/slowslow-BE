import React, { useState } from "react";

function Order() {
  const [productId, setProductId] = useState("");
  const [productCnt, setProductCnt] = useState("");

  const onSubmit = (event) => {
    event.preventDefault();

    // 로컬 스토리지에서 물픔들을 가져옴.
    var storedUsers = JSON.parse(localStorage.getItem("orders"));

    // 만약 비어있으면 새로 만들어서 넣어줌.
    if (storedUsers == null) {
      var order = [
        { productId: productId, productCnt: productCnt, checked: false },
      ];
      localStorage.setItem("orders", JSON.stringify(order));
      return;
    }

    // 안 비어있으면 가져온 json객채 배열에 현재 값을 추가해서 다시 로컬스토리지에 저장함.
    storedUsers.push({
      productId: productId,
      productCnt: productCnt,
      checked: false,
    });
    localStorage.setItem("orders", JSON.stringify(storedUsers));
  };

  function emptyCart() {
    localStorage.clear();

  }

  return (
    <div>
      <h1>주문</h1>
      <form onSubmit={onSubmit}>
        <div>
          <label htmlFor="productId">제품ID</label>
          <input
            type="text"
            id="productId"
            name="productId"
            value={productId}
            onChange={(e) => setProductId(e.target.value)}
          />
        </div>
        <label htmlFor="productCnt">제품수량</label>
        <input
          type="text"
          id="productCnt"
          name="productCnt"
          value={productCnt}
          onChange={(e) => setProductCnt(e.target.value)}
        />
        <div></div>
        <button>장바구니에 담기</button>
      </form>
      <button onClick={emptyCart}> 장바구니 비우기 </button>
    </div>
  );
}

export default Order;
