!function (e) {
    "use strict";
    var t = document, n = "getElementsByClassName", i = function (e) {
        return t.querySelectorAll(e)
    }, s = {type: 0, shade: !0, shadeClose: !0, fixed: !0, anim: "scale"}, a = {
        extend: function (e) {
            var t = JSON.parse(JSON.stringify(s));
            for (var n in e) t[n] = e[n];
            return t
        }, timer: {}, end: {}
    };
    a.touch = function (e, t) {
        e.addEventListener("click", function (e) {
            t.call(this, e)
        }, !1)
    };
    var l = 0, r = ["layui-m-layer"], o = function (e) {
        var t = this;
        t.config = a.extend(e), t.view()
    };
    o.prototype.view = function () {
        var e = this, s = e.config, a = t.createElement("div");
        e.id = a.id = r[0] + l, a.setAttribute("class", r[0] + " " + r[0] + (s.type || 0)), a.setAttribute("index", l);
        var o = function () {
            var e = "object" == typeof s.title;
            return s.title ? '<h3 style="' + (e ? s.title[1] : "") + '">' + (e ? s.title[0] : s.title) + "</h3>" : ""
        }(), c = function () {
            "string" == typeof s.btn && (s.btn = [s.btn]);
            var e, t = (s.btn || []).length;
            return 0 !== t && s.btn ? (e = '<span yes type="1">' + s.btn[0] + "</span>", 2 === t && (e = '<span no type="0">' + s.btn[1] + "</span>" + e), '<div class="layui-m-layerbtn">' + e + "</div>") : ""
        }();
        if (s.fixed || (s.top = s.hasOwnProperty("top") ? s.top : 100, s.style = s.style || "", s.style += " top:" + (t.body.scrollTop + s.top) + "px"), 2 === s.type && (s.content = '<i></i><i class="layui-m-layerload"></i><i></i><p>' + (s.content || "") + "</p>"), s.skin && (s.anim = "up"), "msg" === s.skin && (s.shade = !1), a.innerHTML = (s.shade ? "<div " + ("string" == typeof s.shade ? 'style="' + s.shade + '"' : "") + ' class="layui-m-layershade"></div>' : "") + '<div class="layui-m-layermain" ' + (s.fixed ? "" : 'style="position:static;"') + '><div class="layui-m-layersection"><div class="layui-m-layerchild ' + (s.skin ? "layui-m-layer-" + s.skin + " " : "") + (s.className ? s.className : "") + " " + (s.anim ? "layui-m-anim-" + s.anim : "") + '" ' + (s.style ? 'style="' + s.style + '"' : "") + ">" + o + '<div class="layui-m-layercont">' + s.content + "</div>" + c + "</div></div></div>", !s.type || 2 === s.type) {
            var d = t[n](r[0] + s.type);
            d.length >= 1 && layer.close(d[0].getAttribute("index"))
        }
        document.body.appendChild(a);
        var y = e.elem = i("#" + e.id)[0];
        s.success && s.success(y), e.index = l++, e.action(s, y)
    }, o.prototype.action = function (e, t) {
        var i = this;
        e.time && (a.timer[i.index] = setTimeout(function () {
            layer.close(i.index)
        }, 1e3 * e.time));
        var s = function () {
            0 == this.getAttribute("type") ? (e.no && e.no(), layer.close(i.index)) : e.yes ? e.yes(i.index) : layer.close(i.index)
        };
        if (e.btn) for (var l = t[n]("layui-m-layerbtn")[0].children, r = l.length, o = 0; r > o; o++) a.touch(l[o], s);
        if (e.shade && e.shadeClose) {
            var c = t[n]("layui-m-layershade")[0];
            a.touch(c, function () {
                layer.close(i.index, e.end)
            })
        }
        e.end && (a.end[i.index] = e.end)
    }, e.layer = {
        v: "2.0", index: l, open: function (e) {
            return new o(e || {}).index
        }, close: function (e) {
            var n = i("#" + r[0] + e)[0];
            n && (n.innerHTML = "", t.body.removeChild(n), clearTimeout(a.timer[e]), delete a.timer[e], "function" == typeof a.end[e] && a.end[e](), delete a.end[e])
        }, closeAll: function () {
            for (var e = t[n](r[0]), i = 0, s = e.length; s > i; i++) layer.close(0 | e[0].getAttribute("index"))
        }
    }, "function" == typeof define ? define(function () {
        return layer
    }) : function () {
        var e = document.scripts, n = e[e.length - 1], i = n.src, s = i.substring(0, i.lastIndexOf("/") + 1);
        n.getAttribute("merge") || document.head.appendChild(function () {
            var e = t.createElement("link");
            return e.href = s + "../../css/layer.css", e.type = "text/css", e.rel = "styleSheet", e.id = "layermcss", e
        }())
    }()
}(window);