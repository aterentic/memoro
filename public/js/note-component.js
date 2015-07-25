var NoteItem  = React.createClass({
    render: function() {
	return (
	    <li className="note-item">
		{this.props.text}
	    </li>
	);
    }
});

var Note = React.createClass({
    getInitialState: function() {
	return {data: {items: []}};
    },
    componentDidMount: function () {
	var url = "notes/" + location.queryParams["note"] + "/items";
	$.get(url).done(function(data) {
	    console.log(data);
	    this.setState({data: {items: data}});
	}.bind(this));
    },
    render: function() {
	var items = this.state.data.items.map(function(item) {
	    console.log(item);
	    return (
		    <NoteItem text={item.text} />
	    );
	});
	return (
	    <ul className="note">
		{items}
	    </ul>
	);
    }
});

React.render(
    <Note/>,
    document.getElementById('content')
);

