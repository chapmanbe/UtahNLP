class ConTextItem(object):
    def __init__(self, literal, category, regex, rule):
        if not regex:
            regex = literal
        self.__vals = (literal,
                       category.lower().split(","),
                       re.compile(ur"""%s"""%regex,re.IGNORECASE),
                       rule)

    def literal(self):
        return self.__vals(0)
    def category(self):
        return self.__vals(1)
    def regex(self):
        return self.__vals(2)
    def rule(self):
        return self.__vals(3)

    def __unicode__(self):
        return u"""ContextItem"""
    def isA(self,queryCategory):
        categories = self.category()
        try:
            return queryCategory.lower().strip() in categories
        except:
            for tc in queryCategory:
                if tc.lower().strip() in categories:
                    return True
            return False

class ItemData(object):
    def __init__(self, header = None, items = None):
        if header is None:
            header = ()
        if items is None:
            items = ()
        self.__header = header
        self.__items = items
    def __unicode__(self):
        return u"""%d Contextitem objects"""%(len(self.__items))

class ConTextTag(object):
    def __init__(self, matchedTerm, matchingItem, scope = None):
        if scope is None:
            scope = getSpan(matchedTerm,matchingItem)
        self.__vals = (0,matchedTerm,matchingItem,scope)
    def start(self):
        return self.__vals[1].start()
    def end(self):
        return self.__vals[1].end()
    def foundPhrase(self):
        return self.__vals[1].group()
    def category(self):
        return self.__vals[2].category()
    def rule(self):
        return self.__vals[2].rule()
    def __unicode__(self):
        return u"""ConTextTag"""

