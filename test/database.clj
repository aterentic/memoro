(ns memoro.database
  (:use midje.sweet))

(facts "about 'strip-namespace'"
       (fact "keyword without namespace is returned (part of keyword after '/')."
             (strip-namespace :ns/keyword) => :keyword
             (strip-namespace :other-ns/other-keyword) => :other-keyword))
