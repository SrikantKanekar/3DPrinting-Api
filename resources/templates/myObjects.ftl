<#import "template.ftl" as layout />
<#import "header.ftl" as header />
<@layout.main title="My Objects" css="" js="">
    
    <@header.header user="${user}" />

    <div class="container" style="padding-top: 110px">
        <h2>My Objects</h2>
        
        <#if objects?has_content>
            <#list objects as object>
                <div class="card">
                    <div class="card-header">
                        ID : ${object.id}
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">${object.filename}</h5>
                        <a href="/object/${object.id}" class="btn btn-primary">view</a>
                        <a href="/my-objects/${object.id}/cart" class="btn btn-primary ">Add to cart</a>
                        <a href="/my-objects/${object.id}/delete" class="btn btn-primary ">Delete</a>
                    </div>
                </div>
            </#list>
        <#else>
            <div class="m-5">
                <h4>No objects</h4>
            <div>
        </#if>

    </div>
</@layout.main>