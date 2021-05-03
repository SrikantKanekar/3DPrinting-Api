<#import "template.ftl" as layout />
<#import "header.ftl" as header />
<@layout.main title="Checkout" css="" js="">
    
    <@header.header user="${user}" />

    <div class="container" style="padding-top: 110px">
        <h2>Checkout</h2>
        
        <#if orders?has_content>
            <#list orders as order>
                <div class="card">
                    <div class="card-header">
                        ID : ${order.id}
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">${order.fileName}</h5>
                        <a href="/order/${order.id}" class="btn btn-primary stretched-link">view</a>
                        <a href="/checkout/${order.id}/remove" class="btn btn-primary ">remove</a>
                    </div>
                </div>
            </#list>

            <form action="/checkout/pay" method="post" class="m-5">
                <div class="mb-3">
                    <label for="country" class="form-label">Country</label>
                    <input type="text" name="country" value="${address.country}" class="form-control" id="country">
                </div>
                <div class="mb-3">
                    <label for="state" class="form-label">State</label>
                    <input type="text" name="state" value="${address.state}" class="form-control" id="state">
                </div>
                <div class="mb-3">
                    <label for="city" class="form-label">City</label>
                    <input type="text" name="city" value="${address.city}" class="form-control" id="city">
                </div>
                <button type="submit" class="btn btn-primary">Proceed to pay</button>
            </form>
        <#else>
            <div class="m-5">
                <h4>No checkout items</h4>
            <div>
        </#if>

    </div>
</@layout.main>